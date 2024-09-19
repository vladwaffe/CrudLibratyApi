package com.booktestapi.controller;



import com.booktestapi.DTO.BookDTO;
import com.booktestapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Книги", description = "Взаимодействие с книгами")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping     //    /books
    @Operation(summary = "Получение всех книг сохраненных в бд")
    public ResponseEntity<List<BookDTO>> findAll() {
            List<BookDTO> books = bookService.findAll();
            return ResponseEntity.ok(books);
    }

    @PostMapping
    @Operation(summary = "Создание книги", description = "Позволяет создать новую книгу")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO book) {
            BookDTO savedBook = bookService.saveBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping("/{id}")  ///   /books/id
    @Operation(summary = "Метод удаления книги")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
            bookService.deleteById(id);
            return ResponseEntity.noContent().build();
    }


    @PutMapping()
    @Operation(summary = "Сохранение изменений данных о книге")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO book) {
            BookDTO updatedBook = bookService.updateBook(book);
            return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Поиск книги по ID", description = "Позволяет найти книгу введя её ID")
    @GetMapping("/{id}")    // /books/id
    public ResponseEntity<BookDTO> findBookById(@PathVariable Long id) {
            BookDTO book = bookService.findById(id);
            if (book != null) {
                return ResponseEntity.ok(book);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
    }




    @PostMapping("/find-by-isbn")
    @Operation(summary = "Поиск книги по ISBN", description = "Позволяет найти книгу введя её ISBN")
    public ResponseEntity<List<BookDTO>> findBookByIsbn(@RequestBody String isbn) {  //body object Record
            List<BookDTO> books = bookService.findByIsbn(isbn);
            return ResponseEntity.ok(books);

    }



    @GetMapping("/free")
    @Operation(summary = "Получение списка всех свободных книг")
    public ResponseEntity<List<BookDTO>> getFreeBookList() {    // pageable object
            List<BookDTO> books = bookService.getFreeBookList();
            return ResponseEntity.ok(books);
    }



}




