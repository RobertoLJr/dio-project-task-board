package br.com.dio.dto;

import br.com.dio.persistence.entity.BoardColumnCategoryEnum;

public record BoardColumnDTO(
        Long id,
        String name,
        BoardColumnCategoryEnum kind,
        int cardsAmount) {
}