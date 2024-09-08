package com.my.testcrudapp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
@Schema(description = "Сущность книги")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ISBN", example = "978-5-699-12014-7")
    private String isbn;
    @Schema(description = "Название", example = "Метро 2033")
    private String title;
    @Schema(description = "Жанр", example = "Фантастика")
    private String genre;
    @Schema(description = "Описание", example = "Люди выживают в метро после ядерной войны.")
    private String description;
    @Schema(description = "Автор", example = "Дмитрий Глуховский")
    private String author;



}
