package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
@Import({JpqlBookRepository.class, JpqlAuthorRepository.class})
class JpqlBookRepositoryTest {

    @Autowired
    private JpqlBookRepository jpqlBookRepository;

    @Autowired
    private TestEntityManager em;

    private static final long FIRST_BOOK_ID = 1;
//    private static final Set<Long> AUTHORS_LIST = Set.of(1L, 2L);

    private List<Author> dbAuthors;
    private List<Genre> dbGenres;
//    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
//        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {

        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        Optional<Book> actualBook = jpqlBookRepository.findById(expectedBook.getId());

        assertThat(actualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {

        List<Book> expectedBooks = em.getEntityManager()
                .createQuery("""
                        select b
                          from Book b
                          join fetch b.genre
                          left join fetch b.authors
                        """)
                .getResultList();
        List<Book> actualBooks = jpqlBookRepository.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {

        Book expectedBook = new Book(
                0,
                "BookTitle_10500",
                List.of(dbAuthors.get(0), dbAuthors.get(2)),
                dbGenres.get(0)
        );
        Book actualBook = jpqlBookRepository.save(expectedBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        assertThat(jpqlBookRepository.findById(actualBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(actualBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {

        String bookTitle = "bla bla";
        List<Author> bookAuthors = new ArrayList<>();
        bookAuthors.add(dbAuthors.get(0));
        bookAuthors.add(dbAuthors.get(3));
        Genre bookGenre = dbGenres.get(1);

        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        expectedBook.setTitle(bookTitle);
        expectedBook.setAuthors(bookAuthors);
        expectedBook.setGenre(bookGenre);

        em.persist(expectedBook);

        Book actualBook = jpqlBookRepository.findById(FIRST_BOOK_ID).get();
        actualBook.setTitle(bookTitle);
        actualBook.setAuthors(bookAuthors);
        actualBook.setGenre(bookGenre);
        actualBook = jpqlBookRepository.save(actualBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        assertThat(jpqlBookRepository.findById(actualBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(actualBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(jpqlBookRepository.findById(FIRST_BOOK_ID)).isPresent();
        jpqlBookRepository.deleteById(FIRST_BOOK_ID);
        assertThat(jpqlBookRepository.findById(FIRST_BOOK_ID)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.subList((id - 1) * 2, (id - 1) * 2 + 2),
                        dbGenres.get(id - 1)
                ))
                .toList();
    }

}
