package com.booktestapi.Mapper;


import com.booktestapi.DTO.BookDTO;
import com.booktestapi.model.Book;

public class BookMapper {
    public static BookDTO toDTO(Book book, boolean status) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setStatus(status);
        return bookDTO;
    }

    public static Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        return book;
    }
}
