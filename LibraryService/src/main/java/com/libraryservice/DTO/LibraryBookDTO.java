package com.libraryservice.DTO;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class LibraryBookDTO {
    private Long bookid;
    private Timestamp borrowedtime;
    private Timestamp returntime;

}
