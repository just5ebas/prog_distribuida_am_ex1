package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;

public interface IBookService {

    public Book findById(Integer id);

    public List<Book> findAll();

    public Book create(Book book);

    public Book update(Book book);

    public void delete(Integer id);

}
