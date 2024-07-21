package ru.otus.hw.services.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.*;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookUpdateServiceTest {

    @Autowired
    BookUpdateService bookUpdateService;

    @MockBean
    BookService bookService;
    @MockBean
    BookRepository bookRepository;
    @MockBean
    AuthorService authorService;
    @MockBean
    GenreService genreService;


    private List<Author> authors;
    private List<AuthorDto> authorsDto;
    private List<Genre> genres;
    private List<GenreDto> genresDto;
    private List<Book> books;
    private List<BookDto> booksDto;
    private List<BookComment> dbBookComments;


    @BeforeEach
    void setUp() {
        authors = MockedEntities.authors;
        authorsDto = MockedEntities.authorsDto;
        genres = MockedEntities.genres;
        genresDto = MockedEntities.genresDto;
        books = MockedEntities.books;
        booksDto = MockedEntities.booksDto;
        dbBookComments = getDbBookComments();
    }

    static class MockedEntities {

        public static final List<Genre> genres = LongStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
        public static final List<GenreDto> genresDto = genres.stream()
                .map(GenreDto::new)
                .toList();

        public static final List<Author> authors = LongStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
        public static final List<AuthorDto> authorsDto = authors.stream()
                .map(AuthorDto::new)
                .toList();

        public static final List<Book> books = IntStream.range(1, 7).boxed()
                .map(id -> new Book(Long.valueOf(id), "Title_" + id, List.of(authors.get(id - 1)), genres.get(id - 1)))
                .toList();
        public static final List<BookDto> booksDto = books.stream()
                .map(BookDto::new)
                .toList();
    }

    @Test
    void getTemplateVariablesEditBookFormTest() {

        BookDto book = booksDto.get(0);

        long bookId = book.getId();

        List<AuthorDto> authorsList = authorsDto;

        List<BookEditAuthorDto> templateAuthorsList = MockedEntities.authorsDto.stream()
                .map(BookEditAuthorDto::new)
                .toList();
        templateAuthorsList.get(0).setSelected(true);
        templateAuthorsList.get(2).setSelected(true);

        List<GenreDto> genresList = genresDto;
        List<BookEditGenreDto> templateGenresList = genresList.stream()
                .map(BookEditGenreDto::new)
                .toList();
        templateGenresList.get(0).setSelected(true);

        Map<String, Object> expectedTemplateVariables = Map.of(
                "book", book,
                "authors", templateAuthorsList,
                "genres", templateGenresList
        );

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.findAll()).thenReturn(authorsList);
        when(genreService.findAll()).thenReturn(genresList);

        Map<String, Object> actualTemplateVariables = bookUpdateService.getTemplateVariables(bookId);

        assertThat(actualTemplateVariables)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemplateVariables);

        verify(bookService, times(1)).findById(bookId);
        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
        verifyNoMoreInteractions(bookService, authorService, genreService);
    }

    @Test
    void processUpdateBookTest() {

        Book dbBook = getDbBooks().get(0);
        Book updatedBook = getDbBooks().get(1);
        updatedBook.setId(dbBook.getId());

        when(bookService.findById(dbBook.getId())).thenReturn(Optional.of(new BookDto(dbBook)));

        when(bookRepository.save(dbBook)).thenReturn(updatedBook);

        bookUpdateService.processUpdateBook(updatedBook.getId(), updatedBook);

        verify(bookService, times(1)).findById(dbBook.getId());
        verify(bookRepository, times(1)).save(updatedBook);
        verifyNoMoreInteractions(bookService, bookRepository);
    }


    private static List<Author> getDbAuthors() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Book(Long.valueOf(id), "Title_" + id, List.of(getDbAuthors().get(id - 1)), getDbGenres().get(id - 1)))
                .toList();
    }

    private static List<BookComment> getDbBookComments() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new BookComment(Long.valueOf(id), getDbBooks().get(id - 1), "Comment_" + id))
                .toList();
    }
}