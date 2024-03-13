package ru.otus.hw.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcGenreRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Map<String, Object> queryParams = Collections.singletonMap("id", id);
        var genre = namedParameterJdbcOperations.queryForObject(
                "select id, name from genres where id = :id", queryParams, new GenreRowMapper()
        );

        return Optional.ofNullable(genre);
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long genreId = rs.getLong("id");
            String genreName = rs.getString("name");
            return new Genre(genreId, genreName);
        }
    }
}
