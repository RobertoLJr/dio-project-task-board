package br.com.dio.dto;

import br.com.dio.persistence.entity.BoardColumnCategoryEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnCategoryEnum category) { }