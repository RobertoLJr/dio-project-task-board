package br.com.dio.dto;

import java.time.Instant;

public record CardDetailsDTO(
        Long id,
        String title,
        String description,
        boolean blocked,
        Instant blockedAt,
        String blockCause,
        int blocksAmount,
        Long columnId,
        String columnName
) { }