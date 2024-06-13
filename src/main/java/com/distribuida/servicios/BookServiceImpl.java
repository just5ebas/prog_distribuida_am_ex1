package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements IBookService {

    @Inject
    private EntityManager entityManager;

    @Override
    public Book findById(Integer id) {
        return this.entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> findAll() {
        return this.entityManager.createQuery("SELECT b FROM Book b ORDER BY b.id ASC", Book.class).getResultList();
    }

    @Override
    public Book create(Book book) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public Book update(Book book) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.merge(book);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return book;
    }

    @Override
    public void delete(Integer id) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();
            this.entityManager.remove(this.findById(id));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
