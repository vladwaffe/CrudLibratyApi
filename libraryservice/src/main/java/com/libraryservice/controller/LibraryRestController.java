package com.my.libraryservice.controller;


import com.my.libraryservice.model.LibraryBook;
import com.my.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Rest контроллер книг", description = "Обработка запросов основного сервиса")
public class LibraryRestController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/library/books")
    @Operation(summary = "Добавление книги в бд при добавлении книги в главный сервис")
    public void addBook(@RequestBody Long bookid) {
        libraryService.addBook(bookid);
    }


    @DeleteMapping("/library/delete/{id}")
    @Operation(summary = "Удаление книги из бд при удалении книги из главного сервиса")
    public void deleteBook(@PathVariable("id") Long bookid) {

        libraryService.deleteById(bookid);
    }

    @PostMapping("/library/status")
    @Operation(summary = "Получение статуса книги", description = "Возвращает true если книга в библиотеке и false если на руках")
    public boolean bookStatus(@RequestBody long bookid){
        LibraryBook book = libraryService.findById(bookid);
        if(book.getBorrowedtime() == null){
            return true;
        }
        else{
            return false;
        }
    }





}
