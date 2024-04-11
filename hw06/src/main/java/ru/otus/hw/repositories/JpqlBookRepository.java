package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpqlBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<Book> findById(long id) {

        TypedQuery<Book> query = em.createQuery("""
                        select b
                          from Book b
                          join fetch b.genre
                          left join fetch b.authors
                         where b.id = :id
                        """,
                Book.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());

    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("""
                        select b
                          from Book b
                          join fetch b.genre
                          left join fetch b.authors
                        """,
                Book.class
        );
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {

        Book book = em.find(Book.class, id);

        if (book == null) {
            throw new EntityNotFoundException("No book found for id %d".formatted(id));
        }

        em.remove(book);
    }

}
