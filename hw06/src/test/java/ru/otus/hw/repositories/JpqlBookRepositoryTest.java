package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private static final Set<Long> AUTHORS_LIST = Set.of(1L, 2L);


    private List<Author> dbAuthors;
    private List<Genre> dbGenres;
    private List<Book> dbBooks;


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

        Book entityBook = new Book(
                0,
                "BookTitle_10500",
                List.of(dbAuthors.get(0), dbAuthors.get(2)),
                dbGenres.get(0)
        );
        Book expectedBook = em.persist(entityBook);
        Book returnedBook = jpqlBookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        assertThat(jpqlBookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {

        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        expectedBook.setTitle("bla bla");
        em.persist(expectedBook);

        Optional<Book> actualBookContainer = jpqlBookRepository.findById(FIRST_BOOK_ID);
        Book actualBook = actualBookContainer.get();
        actualBook.setTitle("bla bla");
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
        assertThat(jpqlBookRepository.findById(1L)).isPresent();
        jpqlBookRepository.deleteById(1L);
        assertThat(jpqlBookRepository.findById(1L)).isEmpty();
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

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

}
