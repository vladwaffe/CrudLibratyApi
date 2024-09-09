package com.libraryservice.Mapper;

import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.model.LibraryBook;

public class LibraryBookMapper {
    public static LibraryBookDTO toDTO(LibraryBook book) {
        LibraryBookDTO bookDTO = new LibraryBookDTO();
        bookDTO.setBookid(book.getBookid());
        bookDTO.setBorrowedtime(book.getBorrowedtime());
        bookDTO.setReturntime(book.getReturntime());
        return bookDTO;
    }

    public static LibraryBook toEntity(LibraryBookDTO bookDTO) {
        LibraryBook book = new LibraryBook();
        book.setBookid(bookDTO.getBookid());
        book.setBorrowedtime(bookDTO.getBorrowedtime());
        book.setReturntime(bookDTO.getReturntime());
        return book;
    }


}
