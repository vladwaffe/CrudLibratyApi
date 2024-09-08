package com.booktestapi.controller;



import com.booktestapi.DTO.BookDTO;
import com.booktestapi.model.Book;
import com.booktestapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Tag(name = "Книги", description = "Взаимодействие с книгами")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    @Operation(summary = "Получение всех книг сохраненных в бд")
    public String findAll(Model model){
        List<BookDTO> books = bookService.findAll();
        Book book = new Book();
        model.addAttribute("books", books);
        model.addAttribute("book", book);
        return "book-list";
    }

    @GetMapping("/book-create")
    @Operation(summary = "Создание книги", description = "Позволяет создать новую книгу")
    public String createBookForm(BookDTO book){
        return "book-create";
    }

    @Operation(summary = "Добавление созданной книги в бд")
    @PostMapping("/book-create")
    public String createBook(BookDTO book){
        bookService.saveBook(book);
        return "redirect:books";
    }

    @GetMapping("/book-delete/{id}")
    @Operation(summary = "Метод удаления книги")
    public String deleteBookForm(@PathVariable("id") Long id){
        bookService.deleteById(id);
        return "redirect:http://localhost:8080/books";
    }



    @Operation(summary = "Обновление данных о книге", description = "Позволяет изменить данные книги")
    @GetMapping("/book-update/{id}")
    public String updateBookForm(@PathVariable("id") Long id, Model model){
        BookDTO book = bookService.findById(id);
        model.addAttribute("book", book);
        return "book-update";
    }

    @Operation(summary = "Сохранение изменений данных о книге")
    @PostMapping("/book-update")
    public String updateBook(BookDTO book){
        bookService.updateBook(book);
        return "redirect:books";
    }


    @Operation(summary = "Поиск книги по ID", description = "Позволяет найти книгу введя её ID")
    @PostMapping("/find-book-id")
    public String findBookIdForm(Long id, Model model){
        BookDTO book = bookService.findById(id);
        return bookService.checkBookNull(book, model);
    }

    @Operation(summary = "Поиск книги по ISBN", description = "Позволяет найти книгу введя её ISBN")
    @PostMapping("/find-book-isbn")
    public String findBookIsbnForm(String isbn, Model model){
        List<BookDTO> books = bookService.findByIsbn(isbn);
        return bookService.checkBookListNull(books, model);
    }

    @GetMapping("/book-info/{id}")
    @Operation(summary = "Получение информации о конкретно выбранной книге")
    public String bookInfoForm(@PathVariable("id") Long id, Model model){
        return bookService.getInfoList(id, model);
    }


    @GetMapping("/books-free")
    @Operation(summary = "Получение списка всех свободных книг")
    public String freeBookList(Model model){
        List<BookDTO> books = bookService.getFreeBookList();
        model.addAttribute("books", books);
        return "free-book-list";
    }



}
