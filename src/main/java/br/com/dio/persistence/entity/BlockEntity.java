package br.com.dio.persistence.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class BlockEntity {
    private Long id;
    private Instant blockedAt;
    private String block_cause;
    private Instant unblockedAt;
    private String unblock_cause;
}
