package ru.otus.hw.services.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ListBooksServiceTest {

    @Autowired
    ListBooksService listBooksService;

    @MockBean
    BookService bookService;


    private List<Author> dbAuthors;
    private List<Genre> dbGenres;
    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks();
    }


    @Test
    void getTemplateVariablesListAllBooksTest() {

        List<BookDto> booksList = dbBooks.stream()
                .map(BookDto::new)
                .toList();

        Map<String, Object> expectedTemplateVariables = Map.of("books", booksList);

        when(bookService.findAll()).thenReturn(booksList);

        Map<String, Object> actualTemplateVariables = listBooksService.getTemplateVariablesListAllBooks();

        assertThat(actualTemplateVariables)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemplateVariables);
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
                .map(id -> new Book(id, "Title_" + id, List.of(getDbAuthors().get(id-1)), getDbGenres().get(id-1)))
                .toList();
    }

}