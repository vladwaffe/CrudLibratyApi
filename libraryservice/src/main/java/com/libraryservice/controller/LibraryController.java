package com.libraryservice.controller;

import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@Controller
@Tag(name = "Книги в библиотеке", description = "Взаимодействие с книгами в библиотеке")
public class LibraryController {

    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/book-time-update/{id}")
    @Operation(summary = "Изменение данных о времени получения и возврата книги")
    public String editBookForm(@PathVariable("id") Long id, Model model){
        try {
            LibraryBookDTO book = libraryService.findById(id);
            if(book == null){
                return "redirect:http://localhost:8080/books";
            }
            if(book.getBorrowedtime() == null){
                book.setBorrowedtime(new Timestamp(0));
                book.setReturntime(new Timestamp(0));
            }
            model.addAttribute("book", book);
            return "book-time-update";
        } catch (Exception e) {
            logger.error("Error editing book form", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to edit book form", e);
        }
    }

    @PostMapping("/book-time-update")
    @Operation(summary = "Сохранение измененных данных о книге")
    public String updateBook(LibraryBookDTO book){
        try {
            libraryService.saveBook(book);
            return "redirect:http://localhost:8080/books";
        } catch (Exception e) {
            logger.error("Error updating book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update book", e);
        }
    }

    @GetMapping("/book-time-add-2-week/{id}")
    @Operation(summary = "Взять книгу на 2 недели", description = "Метод позволяет взять книгу на 2 недели " +
            "( автоматически сохраняет в бд текущее время и плюсуя 2 недели время к которому надо вернуть )")
    public String addTwoWeek(@PathVariable("id") Long id){
        try {
            libraryService.addTwoWeek(id);
            return "redirect:http://localhost:8080/book-info/" + id;
        } catch (Exception e) {
            logger.error("Error adding two weeks to book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add two weeks to book", e);
        }
    }

    @GetMapping("/book-time-return/{id}")
    @Operation(summary = "Вернуть книгу", description = "Метод позволяет вернуть книгу" +
            "( автоматически обнуляет время взятия и возврата )")
    public String returnBook(@PathVariable("id") Long id){
        try {
            libraryService.returnBook(id);
            return "redirect:http://localhost:8080/book-info/" + id;
        } catch (Exception e) {
            logger.error("Error returning book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to return book", e);
        }
    }
}
