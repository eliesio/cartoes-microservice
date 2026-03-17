#!/bin/sh
set -eu

script_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
properties_file="$script_dir/.mvn/wrapper/maven-wrapper.properties"

distribution_url=$(grep '^distributionUrl=' "$properties_file" | sed 's/^distributionUrl=//')
[ -n "$distribution_url" ] || {
  echo "cannot read distributionUrl property in .mvn/wrapper/maven-wrapper.properties" >&2
  exit 1
}

if [ -n "${MVNW_REPOURL:-}" ]; then
  case "$distribution_url" in
    *"/org/apache/maven/"*)
      suffix=${distribution_url#*"/org/apache/maven/"}
      distribution_url="${MVNW_REPOURL%/}/org/apache/maven/$suffix"
      ;;
  esac
fi

distribution_name=${distribution_url##*/}
distribution_dir=${distribution_name%.zip}
distribution_dir=${distribution_dir%-bin}

if [ -n "${MAVEN_USER_HOME:-}" ]; then
  maven_user_home="$MAVEN_USER_HOME"
else
  maven_user_home="$HOME/.m2"
fi

hash_value=$(printf '%s' "$distribution_url" | sha256sum | awk '{print $1}')
maven_home="$maven_user_home/wrapper/dists/$distribution_dir/$hash_value"
mvn_cmd="$maven_home/bin/mvn"

if [ ! -x "$mvn_cmd" ]; then
  tmp_dir=$(mktemp -d)
  trap 'rm -rf "$tmp_dir"' EXIT INT TERM
  archive_path="$tmp_dir/$distribution_name"

  mkdir -p "$maven_user_home/wrapper/dists/$distribution_dir"

  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$distribution_url" -o "$archive_path"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$distribution_url" -O "$archive_path"
  else
    echo "curl or wget is required to download Maven" >&2
    exit 1
  fi

  unzip -q "$archive_path" -d "$tmp_dir"
  extracted_dir=$(find "$tmp_dir" -mindepth 1 -maxdepth 1 -type d | head -n 1)
  mv "$extracted_dir" "$maven_home"
  mvn_cmd="$maven_home/bin/mvn"
fi

exec "$mvn_cmd" "$@"