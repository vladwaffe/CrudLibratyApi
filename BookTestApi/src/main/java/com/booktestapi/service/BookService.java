package com.booktestapi.service;


import com.booktestapi.DTO.BookDTO;
import com.booktestapi.Mapper.BookMapper;
import com.booktestapi.hibernate.HibernateUtils;
import com.booktestapi.model.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.*;
import org.springframework.ui.Model;



import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {


    private final ConnectService connectService;

    @Autowired
    public BookService(ConnectService connectService) {
        this.connectService = connectService;
    }


    public String checkBookNull(BookDTO bookDTO, Model model) {
        if (bookDTO == null) {
            return "not-found";
        } else {
            model.addAttribute("book", bookDTO);
            return "book";
        }
    }

    public String checkBookListNull(List<BookDTO> bookDTOs, Model model) {
        if (bookDTOs.size() == 0) {
            return "not-found";
        } else {
            model.addAttribute("books", bookDTOs);
            return "findBookIsbn";
        }
    }

    public String getInfoList(Long id, Model model) {
        BookDTO book = findById(id);
        model.addAttribute("book", book);
        return "book";
    }

    public BookDTO findById(Long id) {
        Book book = HibernateUtils.startSession().get(Book.class, id);
        HibernateUtils.closeSession();
        boolean status = connectService.gelStatusOfBook(book);
        return BookMapper.toDTO(book, status);
    }

    public void saveBook(BookDTO bookDTO) {
        Book book = BookMapper.toEntity(bookDTO);
        Session session = HibernateUtils.startSession();
        session.getTransaction().begin();
        BookDTO testBook = findByIsbn(book.getIsbn()).get(1);
        if (testBook == null) {
            session.persist(book);
            session.getTransaction().commit();
            connectService.addBook(book);
        }
        HibernateUtils.closeSession();
    }

    public List<BookDTO> findAll() {
        List<Book> books = HibernateUtils.startSession().createQuery("FROM Book").list();
        HibernateUtils.closeSession();
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            boolean status = connectService.gelStatusOfBook(book);
            bookDTOs.add(BookMapper.toDTO(book, status));
        }
        return bookDTOs;
    }

    public void deleteById(Long id) {
        Book book = HibernateUtils.startSession().get(Book.class, id);
        HibernateUtils.getSession().beginTransaction();
        HibernateUtils.getSession().remove(book);
        HibernateUtils.getSession().getTransaction().commit();
        HibernateUtils.closeSession();
        connectService.deleteBook(id);
    }

    @Operation(summary = "Поиск книги по ISBN")
    public List<BookDTO> findByIsbn(String isbn) {
        String hql = "FROM Book WHERE isbn = :name";
        Query<Book> query = HibernateUtils.startSession().createQuery(hql);
        query.setParameter("name", isbn);
        List<Book> books = query.getResultList();
        HibernateUtils.closeSession();

        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            boolean status = connectService.gelStatusOfBook(book);
            bookDTOs.add(BookMapper.toDTO(book, status));
        }
        return bookDTOs;
    }


    public List<BookDTO> getFreeBookList() {
        List<BookDTO> books = findAll();
        List<BookDTO> freeBookDTOs = new ArrayList<>();
        for (BookDTO book : books) {
            if (book.getStatus()) {
                freeBookDTOs.add(book);
            }
        }
        return freeBookDTOs;
    }


    public void updateBook(BookDTO bookDTO) {
        Book book = BookMapper.toEntity(bookDTO);
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



}
