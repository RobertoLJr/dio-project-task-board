package br.com.dio.persistence.converter;

import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class InstantConverter {

    public static Instant toInstant(final Timestamp value){
        return nonNull(value) ? value.toInstant() : null;
    }

    public static Timestamp toTimestamp(final Instant value){
        return nonNull(value) ? Timestamp.from(value) : null;
    }

}