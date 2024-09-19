package com.booktestapi.service;


import com.booktestapi.DTO.BookDTO;
import com.booktestapi.Mapper.BookMapper;
import com.booktestapi.exceptions.BookAlreadyExistException;
import com.booktestapi.exceptions.BookNotFoundException;
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


    public BookDTO findById(Long id) {
        try {
            Book book = HibernateUtils.startSession().get(Book.class, id);
            HibernateUtils.closeSession();
            if (book == null) {
                return null;
            } else {
                BookDTO bookDTO = BookMapper.INSTANCE.bookToBookDTO(book);
                bookDTO.setStatus(connectService.gelStatusOfBook(book));
                return bookDTO;
            }
        } catch (Exception e) {
            logger.error("Error in findById: ", e);
            return null;
        }
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        Session session = null;
        try {
            Book book = BookMapper.INSTANCE.bookDTOToBook(bookDTO);
            session = HibernateUtils.startSession();
            session.getTransaction().begin();
            List<BookDTO> books = findByIsbn(book.getIsbn());
            if (books.size() == 0) {
                session.persist(book);
                session.getTransaction().commit();
                connectService.addBook(book);
            }
            else{
                throw new BookAlreadyExistException("Книга уже существует: " + book.getTitle());
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
        return bookDTO;
    }

    public List<BookDTO> findAll() {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            List<Book> books = session.createQuery("FROM Book").list();
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (Book book : books) {
                BookDTO bookDTO = BookMapper.INSTANCE.bookToBookDTO(book);
                bookDTO.setStatus(connectService.gelStatusOfBook(book));
                bookDTOs.add(bookDTO);
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
            if(book==null){
                throw new BookNotFoundException("Книга не найдена: " + id);
            }
            else {
                session.beginTransaction();
                session.remove(book);
                session.getTransaction().commit();
                connectService.deleteBook(id);
            }
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
            if(books.size() == 0){
                throw new BookNotFoundException("Книги не найдены");
            }
            else {
                List<BookDTO> bookDTOs = new ArrayList<>();
                for (Book book : books) {
                    BookDTO bookDTO = BookMapper.INSTANCE.bookToBookDTO(book);
                    bookDTO.setStatus(connectService.gelStatusOfBook(book));
                    bookDTOs.add(bookDTO);
                }
                return bookDTOs;
            }
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

    public BookDTO updateBook(BookDTO book) {
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(BookMapper.INSTANCE.bookDTOToBook(book));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            HibernateUtils.closeSession();
        }
        return book;
    }



}
