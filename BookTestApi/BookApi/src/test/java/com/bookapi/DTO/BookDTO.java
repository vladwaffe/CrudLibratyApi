package com.bookapi.DTO;

import lombok.Data;

@Data
public class BookDTO {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String description;
        private String genre;
        private Boolean status;

}
