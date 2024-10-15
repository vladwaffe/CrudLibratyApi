package com.libraryservice.controller;


import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
@Tag(name = "Rest контроллер книг", description = "Обработка запросов основного сервиса")
public class LibraryRestController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    @Operation(summary = "Добавление книги в бд при добавлении книги в главный сервис")
    public ResponseEntity<Void> addBook(@RequestBody Long bookid) {
        libraryService.addBook(bookid);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление книги из бд при удалении книги из главного сервиса")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long bookid) {
        libraryService.deleteById(bookid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/status")
    @Operation(summary = "Получение статуса книги", description = "Возвращает true если книга в библиотеке и false если на руках")
    public boolean bookStatus(@RequestBody long bookid){
        return libraryService.checkStatus(bookid);
    }

    @PutMapping
    @Operation(summary = "Изменение данных о времени получения и возврата книги")
    public ResponseEntity<LibraryBookDTO> editBook(@RequestBody LibraryBookDTO book) {
        libraryService.updateBook(book);
        return ResponseEntity.ok(book);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Данные книги", description = "Метод позволяет получить книгу по ее id")
    public ResponseEntity<LibraryBookDTO> findById(@PathVariable Long id) {
        LibraryBookDTO book = libraryService.findById(id);
        return ResponseEntity.ok(book);
    }





}
