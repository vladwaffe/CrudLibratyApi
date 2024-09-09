package com.booktestapi.service;


import com.booktestapi.DTO.BookDTO;
import com.booktestapi.Mapper.BookMapper;
import com.booktestapi.hibernate.HibernateUtils;
import com.booktestapi.model.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.*;
import org.springframework.ui.Model;



import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {


    private final ConnectService connectService;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(ConnectService connectService) {
        this.connectService = connectService;
    }


    public String checkBookNull(BookDTO bookDTO, Model model) {
        try {
            if (bookDTO == null) {
                return "not-found";
            } else {
                model.addAttribute("book", bookDTO);
                return "book";
            }
        } catch (Exception e) {
            logger.error("Error in checkBookNull: ", e);
            return "error";
        }
    }

    public String checkBookListNull(List<BookDTO> bookDTOs, Model model) {
        try {
            if (bookDTOs.size() == 0) {
                return "not-found";
            } else {
                model.addAttribute("books", bookDTOs);
                return "findBookIsbn";
            }
        } catch (Exception e) {
            logger.error("Error in checkBookListNull: ", e);
            return "error";
        }
    }

    public String getInfoList(Long id, Model model) {
        try {
            BookDTO book = findById(id);
            if (book == null) {
                return "not-found";
            }
            model.addAttribute("book", book);
            return "book";
        } catch (Exception e) {
            logger.error("Error in getInfoList: ", e);
            return "error";
        }
    }

    public BookDTO findById(Long id) {
        try {
            Book book = HibernateUtils.startSession().get(Book.class, id);
            HibernateUtils.closeSession();
            if (book == null) {
                return null;
            } else {
                boolean status = connectService.gelStatusOfBook(book);
                return BookMapper.toDTO(book, status);
            }
        } catch (Exception e) {
            logger.error("Error in findById: ", e);
            return null;
        }
    }

    public void saveBook(BookDTO bookDTO) {
        Session session = null;
        try {
            Book book = BookMapper.toEntity(bookDTO);
            session = HibernateUtils.startSession();
            session.getTransaction().begin();
            List<BookDTO> books = findByIsbn(book.getIsbn());
            if (books.size() == 0) {
                session.persist(book);
                session.getTransaction().commit();
                connectService.addBook(book);
            }
        } catch (Exception e) {
            logger.error("Error in saveBook: ", e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public List<BookDTO> findAll() {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            List<Book> books = session.createQuery("FROM Book").list();
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (Book book : books) {
                boolean status = connectService.gelStatusOfBook(book);
                bookDTOs.add(BookMapper.toDTO(book, status));
            }
            return bookDTOs;
        } catch (Exception e) {
            logger.error("Error in findAll: ", e);
            return new ArrayList<>();
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public void deleteById(Long id) {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            Book book = session.get(Book.class, id);
            session.beginTransaction();
            session.remove(book);
            session.getTransaction().commit();
            connectService.deleteBook(id);
        } catch (Exception e) {
            logger.error("Error in deleteById: ", e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    @Operation(summary = "Поиск книги по ISBN")
    public List<BookDTO> findByIsbn(String isbn) {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            String hql = "FROM Book WHERE isbn = :name";
            Query<Book> query = session.createQuery(hql);
            query.setParameter("name", isbn);
            List<Book> books = query.getResultList();
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (Book book : books) {
                boolean status = connectService.gelStatusOfBook(book);
                bookDTOs.add(BookMapper.toDTO(book, status));
            }
            return bookDTOs;
        } catch (Exception e) {
            logger.error("Error in findByIsbn: ", e);
            return new ArrayList<>();
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public List<BookDTO> getFreeBookList() {
        try {
            List<BookDTO> books = findAll();
            List<BookDTO> freeBookDTOs = new ArrayList<>();
            for (BookDTO book : books) {
                if (book.getStatus()) {
                    freeBookDTOs.add(book);
                }
            }
            return freeBookDTOs;
        } catch (Exception e) {
            logger.error("Error in getFreeBookList: ", e);
            return new ArrayList<>();
        }
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
