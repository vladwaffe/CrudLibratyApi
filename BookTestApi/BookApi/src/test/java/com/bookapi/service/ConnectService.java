package com.bookapi.service;


import com.booktestapi.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ConnectService {

    private final String status_url = "http://libraryservice:8081/library/status";
    private final String add_book_url = "http://libraryservice:8081/library";
    private final String delete_book_url = "http://libraryservice:8081/library/delete/";

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ConnectService.class);

    @Autowired
    public ConnectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addBook(Book book) {
        try {
            restTemplate.postForObject(add_book_url, book.getId(), Book.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error in addBook: ", e);
        } catch (Exception e) {
            logger.error("Error in addBook: ", e);
        }
    }

    public void deleteBook(Long id) {
        try {
            restTemplate.delete(delete_book_url + id);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error in deleteBook: ", e);
        } catch (Exception e) {
            logger.error("Error in deleteBook: ", e);
        }
    }

    public Boolean getStatusOfBook(Book book) {
        try {
            return restTemplate.postForObject(status_url, book.getId(), boolean.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error in gelStatusOfBook: ", e);
            return false;
        } catch (Exception e) {
            logger.error("Error in gelStatusOfBook: ", e);
            return false;
        }
    }
}