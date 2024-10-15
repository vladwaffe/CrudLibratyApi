package com.booktestapi.mapper;

import com.booktestapi.DTO.BookDTO;
import com.booktestapi.DTO.BookRequestDTO;
import com.booktestapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO bookToBookDTO(Book book);
    Book bookDTOToBook(BookDTO bookDTO);
    BookRequestDTO bookToBookRequestDTO(Book book);
    Book bookRequestDTOToBook(BookRequestDTO bookDTO);
    BookRequestDTO bookDTOToBookRequestDTO(BookDTO bookDTO);
    BookDTO bookRequestDTOToBookDTO(BookRequestDTO bookDTO);
}
