package com.libraryservice.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LibraryAnswerDTO {
    private Timestamp borrowedtime;
    private Timestamp returntime;
}
