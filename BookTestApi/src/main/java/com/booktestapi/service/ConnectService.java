package com.booktestapi.service;



import com.booktestapi.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConnectService {

    private final String status_url = "http://libraryservice:8081/library/status";
    private final String add_book_url = "http://libraryservice:8081/library/books";
    private final String delete_book_url = "http://libraryservice:8081/library/delete/";

    private final RestTemplate restTemplate;

    @Autowired
    public ConnectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void addBook(Book book){
        restTemplate.postForObject(add_book_url, book.getId(), Book.class);
    }
    public void deleteBook(Long id){
        restTemplate.delete(delete_book_url + id);
    }
    public Boolean gelStatusOfBook(Book book){
        return restTemplate.postForObject(status_url, book.getId(), boolean.class);
    }


}
