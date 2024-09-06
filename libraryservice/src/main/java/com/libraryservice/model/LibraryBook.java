package com.my.libraryservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "books")
@Schema(description = "Сущность книги (в контексе ее наличия в библиотеке)")
public class LibraryBook {

    @Id
    private Long bookid;
    @Schema(description = "Время взятия", example = "2024-08-30 08:26:20.550000")
    private Timestamp borrowedtime;
    @Schema(description = "Время к которому нужно вернуть", example = "2024-09-13 08:26:20.550000")
    private Timestamp returntime;
}
