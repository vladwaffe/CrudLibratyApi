package com.bookapi.DTO;

import lombok.Data;

@Data
public class BookRequestDTO {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String genre;
}
