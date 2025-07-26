package br.com.dio.persistence.entity;

public enum BoardColumnCategoryEnum {
    TO_DO,
    IN_PROGRESS,
    DONE,
    BLOCKED,
    ARCHIVED;

    public static BoardColumnCategoryEnum fromString(String name) {
        return BoardColumnCategoryEnum.valueOf(name.toUpperCase());
    }
}
