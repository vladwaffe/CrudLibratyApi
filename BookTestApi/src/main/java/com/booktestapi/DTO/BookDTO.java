package com.booktestapi.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookDTO {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String description;
        private String genre;
        private Boolean status;

}
