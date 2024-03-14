package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final AuthorRepository authorRepository;

    private final NamedParameterJdbcOperations jdbc;


    @Override
    public Optional<Book> findById(long id) {

        Map<String, Object> queryParams = Collections.singletonMap("book_id", id);
        String bookSQL = """
                select b.id       as book_id
                     , b.title    as book_title
                     , b.genre_id as genre_id
                     , g.name     as genre_name
                  from books b
                  left join genres g on g.id = b.genre_id
                 where b.id = :book_id
                """;
        Book book = jdbc.queryForObject(bookSQL, queryParams, new BookRowMapper());

        String bookAuthorsSQL = """
                select author_id
                  from book_authors ba
                 where ba.book_id = :book_id
                """;




        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var authors = authorRepository.findAll();
        var relations = getAllGenreRelations();
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
        //...
    }

    private List<Book> getAllBooksWithoutAuthors() {
        return new ArrayList<>();
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return new ArrayList<>();
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres,
                                List<Author> authors,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        //...

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
    }

    private void removeGenresRelationsFor(Book book) {
        //...
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre(
                    rs.getLong("genre_id"),
                    rs.getString("genre_name")
            );

            return new Book(
                    rs.getLong("book_id"),
                    rs.getString("book_title"),
                    new ArrayList<>(),
                    genre
            );

        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                long bookId = rs.getLong("id");
                String bookTitle = rs.getString("title");
                Long bookGenre = rs.getLong("genre_id");
                Genre genre = new Genre(
                        rs.getLong("genre_id"),
                        rs.getString("genre_name")
                );
                Book book = new Book(
                        rs.getLong("book_id"),
                        rs.getString("book_title"),
                        new ArrayList<>(),
                        genre
                );
            }
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
