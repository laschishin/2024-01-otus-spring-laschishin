package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final AuthorRepository authorRepository;

    private final NamedParameterJdbcOperations jdbc;


    @Override
    public Optional<Book> findById(long id) {

        Map<String, Object> queryParams = Collections.singletonMap("book_id", id);

        String bookAuthorsSQL = """
                select a.id
                     , a.full_name
                  from book_authors ba
                  join authors a on a.id = ba.author_id
                 where ba.book_id = :book_id
                """;
        List<Author> bookAuthors = jdbc.query(bookAuthorsSQL, queryParams, new AuthorRowMapper());

        String bookSQL = """
                select b.id       as book_id
                     , b.title    as book_title
                     , b.genre_id as genre_id
                     , g.name     as genre_name
                  from books b
                  left join genres g on g.id = b.genre_id
                 where b.id = :book_id
                """;
        Book book = jdbc.queryForObject(bookSQL, queryParams, new BookWithoutAuthorsRowMapper());

        if (book == null) {
            return Optional.empty();
        }

        book.setAuthors(bookAuthors);

        return Optional.of(book);
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
        return jdbc.query("select id, title, genre_id from books", new BookWithoutAuthorsRowMapper());
    }

    private List<BookAuthorRelation> getAllAuthorRelations() {
        return jdbc.query("select book_id, author_id from book_authors", new BookAuthorRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutAuthors,
                                List<Author> authors,
                                List<BookAuthorRelation> relations) {
        // Добавить книгам (booksWithoutAuthors) жанры (genres) в соответствии со связями (relations)
        booksWithoutAuthors = booksWithoutAuthors.stream()
                .map(book -> enrichBookWithAuthors(book, authors, relations))
                .collect(Collectors.toList());
    }

    private Author filterAuthorById(Long authorId, List<Author> authors) {
        return authors.stream()
                .filter(author -> authorId == author.getId())
                .findFirst().get();
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

        //...

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertAuthorsRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        //...

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        removeGenresRelationsFor(book);
        batchInsertAuthorsRelationsFor(book);

        return book;
    }

    private void batchInsertAuthorsRelationsFor(Book book) {

        List<SqlParameterSource> params = new ArrayList<>();

        for (Author author : book.getAuthors()) {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("book_id", book.getId())
                    .addValue("author_id", author.getId());
//            Map<String, Object> param = new HashMap<String, Object>();
//            param.put("book_id", book.getId());
//            param.put("author_id", author.getId());

            params.add(param);
        }
//
//        Map<String, Object>[] arr = new Map<String, Object>[params.size()];
//
//        Integer arr2[] = new Integer[params.size()];
//
//        SqlParameterSource[] arr3 = new SqlParameterSource[params.size()];

        jdbc.batchUpdate("insert into book_authors(book_id, author_id) values(?, ?)", params.toArray(new SqlParameterSource[params.size()]));
    }

    private void removeGenresRelationsFor(Book book) {
        //...
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

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long authorId = rs.getLong("id");
            String authorFullName = rs.getString("full_name");
            return new Author(authorId, authorFullName);
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
