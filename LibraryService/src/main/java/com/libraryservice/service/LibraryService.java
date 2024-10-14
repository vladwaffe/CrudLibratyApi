package com.libraryservice.service;

import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.mapper.BookMapper;
import com.libraryservice.hibernate.HibernateUtils;
import com.libraryservice.model.LibraryBook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

@Service
public class LibraryService {

    private static final Logger logger = LoggerFactory.getLogger(LibraryService.class);

    public LibraryBookDTO findById(Long id){
        try {
            LibraryBook book = HibernateUtils.startSession().get(LibraryBook.class, id);
            HibernateUtils.closeSession();
            return BookMapper.INSTANCE.bookToBookDTO(book);
        } catch (Exception e) {
            logger.error("Error finding book by ID", e);
            throw new RuntimeException("Unable to find book by ID", e);
        }
    }

    public boolean checkStatus(long bookid){
        LibraryBookDTO book = findById(bookid);
        if(book.getBorrowedtime() == null){
            return true;
        }
        else{
            return false;
        }
    }


    public void saveBook(LibraryBookDTO book){
        Session session = HibernateUtils.startSession();
        try {
            session.getTransaction().begin();
            session.persist(BookMapper.INSTANCE.bookDTOToBook(book));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error saving book", e);
            throw new RuntimeException("Unable to save book", e);
        } finally {
            HibernateUtils.closeSession();
        }
    }


    public void deleteById(Long id){
        Session session = HibernateUtils.startSession();
        try {
            LibraryBook book = session.get(LibraryBook.class, id);
            session.beginTransaction();
            session.remove(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error deleting book", e);
            throw new RuntimeException("Unable to delete book", e);
        } finally {
            HibernateUtils.closeSession();
        }
    }



    public void addBook(Long bookid) {
        try {
            LibraryBookDTO libraryBook = new LibraryBookDTO();
            libraryBook.setBookid(bookid);
            saveBook(libraryBook);
        } catch (Exception e) {
            logger.error("Error adding book", e);
            throw new RuntimeException("Unable to add book", e);
        }
    }


    public void updateBook(LibraryBookDTO bookDTO){
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (bookDTO.getBorrowedtime() == null) {
                bookDTO.setBorrowedtime(new Timestamp(0));
                bookDTO.setReturntime(new Timestamp(0));
            }
            session.merge(BookMapper.INSTANCE.bookDTOToBook(bookDTO));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating book", e);
            throw new RuntimeException("Unable to update book", e);
        } finally {
            HibernateUtils.closeSession();
        }
    }


    public void addTwoWeek(Long bookid){
        try {
            LibraryBookDTO book = findById(bookid);
            book.setBorrowedtime(new Timestamp(System.currentTimeMillis()));
            book.setReturntime(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(14, ChronoUnit.DAYS)));
            updateBook(book);
        } catch (Exception e) {
            logger.error("Error adding two weeks to book", e);
            throw new RuntimeException("Unable to add two weeks to book", e);
        }
    }

    public void returnBook(Long bookid){
        try {
            LibraryBookDTO book = findById(bookid);
            book.setBorrowedtime(null);
            book.setReturntime(null);
            updateBook(book);
        } catch (Exception e) {
            logger.error("Error returning book", e);
            throw new RuntimeException("Unable to return book", e);
        }
    }



}
