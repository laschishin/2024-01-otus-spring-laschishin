package ru.otus.hw.services.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookUpdateServiceTest {

    @Autowired
    BookUpdateService bookUpdateService;

    @MockBean
    BookService bookService;
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
        return IntStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Book(id, "Title_" + id, List.of(getDbAuthors().get(id - 1)), getDbGenres().get(id - 1)))
                .toList();
    }

    private static List<BookComment> getDbBookComments() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new BookComment(id, getDbBooks().get(id - 1), "Comment_" + id))
                .toList();
    }
}