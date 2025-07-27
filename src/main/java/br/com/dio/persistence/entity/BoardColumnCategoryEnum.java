package br.com.dio.persistence.entity;

public enum BoardColumnCategoryEnum {
    INITIAL,
    FINAL,
    CANCEL,
    PENDING;

    public static BoardColumnCategoryEnum fromString(String name) {
        return BoardColumnCategoryEnum.valueOf(name.toUpperCase());
    }
}
