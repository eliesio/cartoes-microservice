param(
    [Parameter(Mandatory = $true)]
    [string]$ScriptName
)

$ErrorActionPreference = 'Stop'

function Get-WrapperProperties {
    param([string]$PropertiesPath)

    $properties = @{}
    foreach ($line in Get-Content -Path $PropertiesPath) {
        $trimmed = $line.Trim()
        if (-not $trimmed -or $trimmed.StartsWith('#')) {
            continue
        }

        $separatorIndex = $trimmed.IndexOf('=')
        if ($separatorIndex -lt 1) {
            continue
        }

        $key = $trimmed.Substring(0, $separatorIndex).Trim()
        $value = $trimmed.Substring($separatorIndex + 1).Trim()
        $properties[$key] = $value
    }

    return $properties
}

function Get-HashString {
    param([string]$Value)

    $sha256 = [System.Security.Cryptography.SHA256]::Create()
    try {
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($Value)
        return (($sha256.ComputeHash($bytes) | ForEach-Object { $_.ToString('x2') }) -join '')
    }
    finally {
        $sha256.Dispose()
    }
}

function Resolve-DistributionUrl {
    param([string]$ConfiguredUrl)

    if (-not $env:MVNW_REPOURL) {
        return $ConfiguredUrl
    }

    $repoBase = $env:MVNW_REPOURL.TrimEnd('/')
    $pattern = '/org/apache/maven/'
    $index = $ConfiguredUrl.IndexOf($pattern)
    if ($index -lt 0) {
        return $ConfiguredUrl
    }

    return $repoBase + $ConfiguredUrl.Substring($index)
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Split-Path -Parent (Split-Path -Parent $scriptDir)
$propertiesPath = Join-Path $scriptDir 'maven-wrapper.properties'
$properties = Get-WrapperProperties -PropertiesPath $propertiesPath

$distributionUrl = Resolve-DistributionUrl -ConfiguredUrl $properties['distributionUrl']
if (-not $distributionUrl) {
    throw 'cannot read distributionUrl property in .mvn/wrapper/maven-wrapper.properties'
}

$distributionUrlName = [System.IO.Path]::GetFileName($distributionUrl)
if (-not $distributionUrlName.EndsWith('-bin.zip')) {
    throw "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
}

$distributionUrlNameMain = [System.IO.Path]::GetFileNameWithoutExtension($distributionUrlName)
if ($distributionUrlNameMain.EndsWith('-bin')) {
    $distributionUrlNameMain = $distributionUrlNameMain.Substring(0, $distributionUrlNameMain.Length - 4)
}

$mavenUserHome = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path $HOME '.m2' }
$wrapperDists = Join-Path $mavenUserHome 'wrapper\dists'
$mavenHomeParent = Join-Path $wrapperDists $distributionUrlNameMain
$mavenHomeName = Get-HashString -Value $distributionUrl
$mavenHome = Join-Path $mavenHomeParent $mavenHomeName
$mvnCommandName = if ($ScriptName -like 'mvnw*') { $ScriptName -replace '^mvnw', 'mvn' } else { 'mvn.cmd' }
$mvnCommandPath = Join-Path $mavenHome (Join-Path 'bin' $mvnCommandName)

if (Test-Path -Path $mvnCommandPath -PathType Leaf) {
    Write-Output $mvnCommandPath
    exit 0
}

New-Item -Path $mavenHomeParent -ItemType Directory -Force | Out-Null
$tempHolder = New-TemporaryFile
$tempDir = New-Item -Path ($tempHolder.FullName + '.dir') -ItemType Directory -Force
$tempHolder.Delete()

try {
    $archivePath = Join-Path $tempDir.FullName $distributionUrlName
    $webClient = New-Object System.Net.WebClient
    if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
        $webClient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
    }

    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $webClient.DownloadFile($distributionUrl, $archivePath)

    $distributionSha256Sum = $properties['distributionSha256Sum']
    if ($distributionSha256Sum) {
        $actualHash = (Get-FileHash -Path $archivePath -Algorithm SHA256).Hash.ToLower()
        if ($actualHash -ne $distributionSha256Sum.ToLower()) {
            throw 'Failed to validate Maven distribution SHA-256.'
        }
    }

    Expand-Archive -Path $archivePath -DestinationPath $tempDir.FullName -Force

    $actualDistributionDir = Get-ChildItem -Path $tempDir.FullName -Directory |
        Where-Object { Test-Path -Path (Join-Path $_.FullName (Join-Path 'bin' $mvnCommandName)) -PathType Leaf } |
        Select-Object -First 1

    if (-not $actualDistributionDir) {
        throw 'Could not find Maven distribution directory in extracted archive'
    }

    $stagedHome = Join-Path $mavenHomeParent $mavenHomeName
    if (Test-Path -Path $stagedHome) {
        Remove-Item -Path $stagedHome -Recurse -Force
    }

    Move-Item -Path $actualDistributionDir.FullName -Destination $stagedHome
    $mvnCommandPath = Join-Path $stagedHome (Join-Path 'bin' $mvnCommandName)

    if (-not (Test-Path -Path $mvnCommandPath -PathType Leaf)) {
        throw 'Cannot resolve Maven executable after extraction'
    }

    Write-Output $mvnCommandPath
}
finally {
    if (Test-Path -Path $tempDir.FullName) {
        Remove-Item -Path $tempDir.FullName -Recurse -Force -ErrorAction SilentlyContinue
    }
}