package br.com.dio.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {
    private Long id;
    private String name;
    private int order;
    private BoardColumnCategoryEnum category;
    private BoardEntity board = new BoardEntity();
}
