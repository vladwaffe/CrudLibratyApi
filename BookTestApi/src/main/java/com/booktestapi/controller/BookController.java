package com.booktestapi.controller;



import com.booktestapi.DTO.BookDTO;
import com.booktestapi.model.Book;
import com.booktestapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@Tag(name = "Книги", description = "Взаимодействие с книгами")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    @Operation(summary = "Получение всех книг сохраненных в бд")
    public String findAll(Model model){
        try {
            List<BookDTO> books = bookService.findAll();
            Book book = new Book();
            model.addAttribute("books", books);
            model.addAttribute("book", book);
            return "book-list";
        } catch (Exception e) {
            logger.error("Error fetching books", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch books", e);
        }
    }

    @GetMapping("/book-create")
    @Operation(summary = "Создание книги", description = "Позволяет создать новую книгу")
    public String createBookForm(BookDTO book, Model model){
        model.addAttribute("book", book);
        return "book-create";
    }

    @Operation(summary = "Добавление созданной книги в бд")
    @PostMapping("/book-create")
    public String createBook(BookDTO book){
        try {
            bookService.saveBook(book);
            return "redirect:books";
        } catch (Exception e) {
            logger.error("Error creating book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create book", e);
        }
    }

    @GetMapping("/book-delete/{id}")
    @Operation(summary = "Метод удаления книги")
    public String deleteBookForm(@PathVariable("id") Long id){
        try {
            bookService.deleteById(id);
            return "redirect:http://localhost:8080/books";
        } catch (Exception e) {
            logger.error("Error deleting book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete book", e);
        }
    }

    @Operation(summary = "Обновление данных о книге", description = "Позволяет изменить данные книги")
    @GetMapping("/book-update/{id}")
    public String updateBookForm(@PathVariable("id") Long id, Model model){
        try {
            BookDTO book = bookService.findById(id);
            model.addAttribute("book", book);
            return "book-update";
        } catch (Exception e) {
            logger.error("Error fetching book for update", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch book for update", e);
        }
    }

    @Operation(summary = "Сохранение изменений данных о книге")
    @PostMapping("/book-update")
    public String updateBook(BookDTO book){
        try {
            bookService.updateBook(book);
            return "redirect:books";
        } catch (Exception e) {
            logger.error("Error updating book", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update book", e);
        }
    }



    @Operation(summary = "Поиск книги по ID", description = "Позволяет найти книгу введя её ID")
    @PostMapping("/find-book-id")
    public String findBookIdForm(Long id, Model model){
        try {
            BookDTO book = bookService.findById(id);
            return bookService.checkBookNull(book, model);
        } catch (Exception e) {
            logger.error("Error finding book by ID", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find book by ID", e);
        }
    }

    @Operation(summary = "Поиск книги по ISBN", description = "Позволяет найти книгу введя её ISBN")
    @PostMapping("/find-book-isbn")
    public String findBookIsbnForm(String isbn, Model model){
        try {
            List<BookDTO> books = bookService.findByIsbn(isbn);
            return bookService.checkBookListNull(books, model);
        } catch (Exception e) {
            logger.error("Error finding book by ISBN", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find book by ISBN", e);
        }
    }

    @GetMapping("/book-info/{id}")
    @Operation(summary = "Получение информации о конкретно выбранной книге")
    public String bookInfoForm(@PathVariable("id") Long id, Model model){
        try {
            return bookService.getInfoList(id, model);
        } catch (Exception e) {
            logger.error("Error fetching book info", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch book info", e);
        }
    }

    @GetMapping("/books-free")
    @Operation(summary = "Получение списка всех свободных книг")
    public String freeBookList(Model model){
        try {
            List<BookDTO> books = bookService.getFreeBookList();
            model.addAttribute("books", books);
            return "free-book-list";
        } catch (Exception e) {
            logger.error("Error fetching free books list", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch free books list", e);
        }
    }
}




