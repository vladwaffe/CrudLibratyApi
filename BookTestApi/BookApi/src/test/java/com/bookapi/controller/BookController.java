package com.bookapi.controller;



import com.booktestapi.DTO.BookDTO;
import com.booktestapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    @Operation(summary = "Получение всех книг сохраненных в бд")
    public ResponseEntity<List<BookDTO>> findAll() {
            List<BookDTO> books = bookService.findAll();
            return ResponseEntity.ok(books);
    }

    @PostMapping
    @Operation(summary = "Создание книги", description = "Позволяет создать новую книгу")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
            BookDTO savedBook = bookService.saveBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping()
    @Operation(summary = "Метод удаления события id")
    public ResponseEntity<Void> deleteBookByID(@RequestBody BookDTO bookDTO) {
        bookService.deleteById(bookDTO);
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
           return ResponseEntity.ok(bookService.findById(id));
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




