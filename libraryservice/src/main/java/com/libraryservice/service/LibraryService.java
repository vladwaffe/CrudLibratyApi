package com.libraryservice.service;

import com.libraryservice.DTO.LibraryBookDTO;
import com.libraryservice.Mapper.LibraryBookMapper;
import com.libraryservice.hibernate.HibernateUtils;
import com.libraryservice.model.LibraryBook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private static final Logger logger = LoggerFactory.getLogger(LibraryService.class);

    public LibraryBookDTO findById(Long id){
        try {
            LibraryBook book = HibernateUtils.startSession().get(LibraryBook.class, id);
            HibernateUtils.closeSession();
            return LibraryBookMapper.toDTO(book);
        } catch (Exception e) {
            logger.error("Error finding book by ID", e);
            throw new RuntimeException("Unable to find book by ID", e);
        }
    }

    @Transactional
    public void saveBook(LibraryBookDTO book){
        Session session = HibernateUtils.startSession();
        try {
            session.getTransaction().begin();
            session.persist(LibraryBookMapper.toEntity(book));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error saving book", e);
            throw new RuntimeException("Unable to save book", e);
        } finally {
            HibernateUtils.closeSession();
        }
    }

    @Transactional
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

    public List<LibraryBookDTO> findAll(){
        try {
            List<LibraryBook> books = HibernateUtils.startSession().createQuery("FROM LibraryBook ").list();
            HibernateUtils.closeSession();
            List<LibraryBookDTO> booksDTO = new ArrayList<>();
            for(LibraryBook book : books){
                booksDTO.add(LibraryBookMapper.toDTO(book));
            }
            return booksDTO;
        } catch (Exception e) {
            logger.error("Error finding all books", e);
            throw new RuntimeException("Unable to find all books", e);
        }
    }

    @Transactional
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

    @Transactional
    public void updateBook(LibraryBookDTO bookDTO){
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(LibraryBookMapper.toEntity(bookDTO));
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

    @Transactional
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

    @Transactional
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
