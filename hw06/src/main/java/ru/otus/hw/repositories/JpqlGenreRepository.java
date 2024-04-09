package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("""
                        select g
                          from Genre g
                        """,
                Genre.class
        );
        return query.getResultList();

    }

    @Override
    public Optional<Genre> findById(long id) {

        return Optional.ofNullable(em.find(Genre.class, id));

    }

}
