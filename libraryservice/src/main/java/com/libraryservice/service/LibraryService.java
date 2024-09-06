package com.my.libraryservice.service;

import com.my.libraryservice.hibernate.HibernateUtils;
import com.my.libraryservice.model.LibraryBook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LibraryService {

    public LibraryBook findById(Long id){
        LibraryBook book = HibernateUtils.startSession().get(LibraryBook.class, id);
        HibernateUtils.closeSession();
        return book;
    }
    public void saveBook(LibraryBook book){
        Session session = HibernateUtils.startSession();
        session.getTransaction().begin();
        session.persist(book);
        session.getTransaction().commit();
        HibernateUtils.closeSession();
    }

    public void deleteById(Long id){
        LibraryBook book = HibernateUtils.startSession().get(LibraryBook.class, id);
        HibernateUtils.getSession().beginTransaction();
        HibernateUtils.getSession().remove(book);
        HibernateUtils.getSession().getTransaction().commit();
        HibernateUtils.closeSession();
    }

    public List<LibraryBook> findAll(){
        List<LibraryBook> books = HibernateUtils.startSession().createQuery("FROM LibraryBook ").list();
        HibernateUtils.closeSession();
        return books;
    }

    public void addBook(Long bookid) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBookid(bookid);
        saveBook(libraryBook);
    }
    public void updateBook(LibraryBook book){
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            HibernateUtils.closeSession();
        }
    }

    public void addTwoWeek(Long bookid){
        LibraryBook book = findById(bookid);
        book.setBorrowedtime(new Timestamp(System.currentTimeMillis()));
        book.setReturntime(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(14, ChronoUnit.DAYS)));
        updateBook(book);
    }

    public void returnBook(Long bookid){
        LibraryBook book = findById(bookid);
        book.setBorrowedtime(null);
        book.setReturntime(null);
        updateBook(book);
    }

}
