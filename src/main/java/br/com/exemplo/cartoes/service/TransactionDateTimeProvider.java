package br.com.exemplo.cartoes.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionDateTimeProvider {

    private final ZoneId zoneId;

    public TransactionDateTimeProvider(@Value("${app.time-zone:America/Sao_Paulo}") String timeZone) {
        this.zoneId = ZoneId.of(timeZone);
    }

    public LocalDateTime now() {
        return LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.SECONDS);
    }
}