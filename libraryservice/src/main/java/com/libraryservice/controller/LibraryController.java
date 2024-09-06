package com.my.libraryservice.controller;

import com.my.libraryservice.model.LibraryBook;
import com.my.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;

@Controller
@Tag(name = "Книги в библиотеке", description = "Взаимодействие с книгами в библиотеке")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/book-time-update/{id}")
    @Operation(summary = "Изменение данных о времени получения и возврата книги")
    public String editBookForm(@PathVariable("id") Long id, Model model){
        LibraryBook book = libraryService.findById(id);
        if(book == null){
            return "redirect:http://localhost:8080/books";
        }
        if(book.getBorrowedtime()==null){
            book.setBorrowedtime(new Timestamp(0));
            book.setReturntime(new Timestamp(0));
        }
        model.addAttribute("book", book);
        return "book-time-update";
    }


    @PostMapping("/book-time-update")
    @Operation(summary = "сохранение измененных данных о книге")
    public String updateBook(LibraryBook book){
        libraryService.saveBook(book);
        return "redirect:http://localhost:8080/books";
    }


    @GetMapping("/book-time-add-2-week/{id}")
    @Operation(summary = "Взять книгу на 2 недели", description = "Метод позволяет взять книгу на 2 недели " +
            "( автоматически сохраняет в бд текушее время и плюсуя 2 недели время к которому надо вернуть )")
    public String addTwoWeek(@PathVariable("id") Long id){
        libraryService.addTwoWeek(id);
        return "redirect:http://localhost:8080/book-info/" + id;
    }
    @GetMapping("/book-time-return/{id}")
    @Operation(summary = "Вернуть книгу", description = "Метод позволяет вернуть книгу" +
            "( автоматически обнуляет время взятия и возврата )")
    public String returnBook(@PathVariable("id") Long id){
        libraryService.returnBook(id);
        return "redirect:http://localhost:8080/book-info/" + id;
    }






}
