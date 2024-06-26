package ru.otus.hw.repositories;

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
public class JdbcBookRepository implements BookRepository {

    private final AuthorRepository authorRepository;

    private final NamedParameterJdbcOperations jdbc;


    @Override
    public Optional<Book> findById(long id) {

        Map<String, Object> queryParams = Collections.singletonMap("book_id", id);

        String bookSQL = """
                select b.id        as book_id
                     , b.title     as book_title
                     , b.genre_id  as genre_id
                     , g.name      as genre_name
                     , a.id        as author_id
                     , a.full_name as author_full_name
                  from books b
                  left join genres g on g.id = b.genre_id
                  left join book_authors ba on ba.book_id = b.id
                  left join authors a on a.id = ba.author_id
                 where b.id = :book_id
                """;

        Book book = jdbc.query(bookSQL, queryParams, new BookResultSetExtractor());

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var authors = authorRepository.findAll();
        var relations = getAllAuthorRelations();
        var books = getAllBooksWithoutAuthors();
        mergeBooksInfo(books, authors, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutAuthors() {
        String bookSQL = """
                select b.id       as book_id
                     , b.title    as book_title
                     , b.genre_id as genre_id
                     , g.name     as genre_name
                  from books b
                  left join genres g on g.id = b.genre_id
                """;

        return jdbc.query(bookSQL, new BookWithoutAuthorsRowMapper());
    }

    private List<BookAuthorRelation> getAllAuthorRelations() {
        return jdbc.query("select book_id, author_id from book_authors", new BookAuthorRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutAuthors,
                                List<Author> authors,
                                List<BookAuthorRelation> relations) {
        booksWithoutAuthors = booksWithoutAuthors.stream()
                .map(book -> enrichBookWithAuthors(book, authors, relations))
                .collect(Collectors.toList());
    }

    private Author filterAuthorById(Long authorId, List<Author> authors) {
        return authors.stream()
                .filter(author -> authorId == author.getId())
                .findFirst()
                .get();
    }

    private Book enrichBookWithAuthors(Book book, List<Author> authors, List<BookAuthorRelation> relations) {
        List<Author> bookAuthors = relations.stream()
                .filter(relation -> book.getId() == relation.bookId)
                .map(relation -> filterAuthorById(relation.authorId, authors))
                .toList();
        book.setAuthors(bookAuthors);
        return book;

    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("genre_id", book.getGenre().getId());

        jdbc.update("insert into books(title, genre_id) values(:title, :genre_id)", params, keyHolder);

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertAuthorsRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("genre_id", book.getGenre().getId())
                .addValue("id", book.getId());

        int updatedRowsCount = jdbc.update("update books set title = :title, genre_id = :genre_id where id = :id", params);

        if (updatedRowsCount == 0) {
            throw new EntityNotFoundException("Book has not been found");
        }

        removeAuthorsRelationsFor(book);
        batchInsertAuthorsRelationsFor(book);

        return book;
    }

    private void batchInsertAuthorsRelationsFor(Book book) {

        List<SqlParameterSource> params = new ArrayList<>();

        for (Author author : book.getAuthors()) {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("book_id", book.getId())
                    .addValue("author_id", author.getId());

            params.add(param);
        }

        jdbc.batchUpdate("insert into book_authors(book_id, author_id) values(:book_id, :author_id)",
                params.toArray(new SqlParameterSource[0]));
    }

    private void removeAuthorsRelationsFor(Book book) {
        Map<String, Object> params = Collections.singletonMap("book_id", book.getId());

        jdbc.update("delete from book_authors where book_id = :book_id", params);
    }

    private static class BookWithoutAuthorsRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre(
                    rs.getLong("genre_id"),
                    rs.getString("genre_name")
            );

            return new Book(
                    rs.getLong("book_id"),
                    rs.getString("book_title"),
                    null,
                    genre
            );
        }

    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {

            Book book = null;
            List<Author> authors = null;

            while (rs.next()) {

                if(rs.getRow() == 1) {
                    book = new Book();
                    authors = new ArrayList<>();
                    book.setId(rs.getLong("book_id"));
                    book.setTitle(rs.getString("book_title"));
                    book.setGenre(new Genre(
                            rs.getLong("genre_id"),
                            rs.getString("genre_name")
                    ));
                }
                authors.add(new Author(
                        rs.getLong("author_id"),
                        rs.getString("author_full_name")
                ));
            }
            if (book == null) {
                return null;
            }
            book.setAuthors(authors);
            return book;
        }
    }

    private static class BookAuthorRelationRowMapper implements RowMapper<BookAuthorRelation> {

        @Override
        public BookAuthorRelation mapRow(ResultSet rs, int i) throws SQLException {
            return new BookAuthorRelation(
                    rs.getLong("book_id"),
                    rs.getLong("author_id")
            );
        }

    }

    private record BookAuthorRelation(long bookId, long authorId) {
    }
}
