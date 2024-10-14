package com.libraryservice.mapper;


import com.libraryservice.DTO.LibraryAnswerDTO;
import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.model.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    LibraryBookDTO bookToBookDTO(LibraryBook book);
    LibraryBook bookDTOToBook(LibraryBookDTO bookDTO);

    LibraryAnswerDTO bookToAnswerDTO(LibraryBook book);
    LibraryBook answerDTOToBook(LibraryAnswerDTO bookDTO);
}
