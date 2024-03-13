package ru.otus.hw.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcAuthorRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        Map<String, Object> queryParams = ids.stream()
                .collect(Collectors.toMap(k -> "id", v -> v));

        return namedParameterJdbcOperations.query(
                "select id, name from genres where id = :id", queryParams, new AuthorRowMapper()
        );
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long authorId = rs.getLong("id");
            String authorFullName = rs.getString("full_name");
            return new Author(authorId, authorFullName);
        }
    }
}
