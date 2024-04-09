package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpqlAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;
    
    
    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("""
                        select a
                          from Author a
                        """,
                Author.class
        );
        return query.getResultList();
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        TypedQuery<Author> query = em.createQuery("""
                        select a
                          from Author a
                         where a.id in (:ids)
                        """,
                Author.class
        );
        query.setParameter("ids", ids);
        return query.getResultList();
    }

}
