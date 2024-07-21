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
import ru.otus.hw.services.BookCommentService;
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
@MockBean(value = {
        AuthorService.class
})
class BookDeleteServiceTest {

    @Autowired
    BookDeleteService bookDeleteService;

    @MockBean
    AuthorService authorService;
    @MockBean
    GenreService genreService;
    @MockBean
    BookService bookService;
    @MockBean
    BookCommentService bookCommentService;
    @MockBean
    BookRepository bookRepository;


    private List<Author> dbAuthors;
    private List<Genre> dbGenres;
    private List<Book> dbBooks;
    private List<BookComment> dbBookComments;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks();
        dbBookComments = getDbBookComments();
    }


    @Test
    void getTemplateVariablesDeleteBookTest() {

        BookDto book = new BookDto(dbBooks.get(0));
        long bookId = book.getId();

        Map<String, Object> expectedTemplateVariables = Map.of(
                "book", book,
                "authors_list", book.getAuthors(),
                "genre", book.getGenre()
        );

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        Map<String, Object> actualTemplateVariables = bookDeleteService.getTemplateVariablesDeleteBook(bookId);

        assertThat(actualTemplateVariables)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemplateVariables);

        verify(bookService, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    void processDeleteBookTest() {

        Book book = new Book();
        long bookId = book.getId();

        bookDeleteService.processDeleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
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
