package ru.otus.hw.services.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookEditAuthorDto;
import ru.otus.hw.dto.BookEditGenreDto;
import ru.otus.hw.dto.GenreDto;
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
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockBean(value = {
        AuthorService.class
})
class BookCreateServiceTest {

    @Autowired
    BookCreateService bookCreateService;

    @MockBean
    AuthorService authorService;
    @MockBean
    GenreService genreService;


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
    void getTemplateVariablesEmptyBookTest() {
        List<AuthorDto> authorsList = dbAuthors.stream()
                .map(AuthorDto::new)
                .toList();
        List<BookEditAuthorDto> templateAuthorsList = authorsList.stream()
                .map(BookEditAuthorDto::new)
                .toList();

        List<GenreDto> genresList = dbGenres.stream()
                .map(GenreDto::new)
                .toList();
        List<BookEditGenreDto> templateGenresList = genresList.stream()
                .map(BookEditGenreDto::new)
                .toList();

        Map<String, Object> expectedTemplateVariables = Map.of(
                "authors", templateAuthorsList,
                "genres", templateGenresList
        );

        when(authorService.findAll()).thenReturn(authorsList);
        when(genreService.findAll()).thenReturn(genresList);

        Map<String, Object> actualTemplateVariables = bookCreateService.getTemplateVariablesEmptyBook();

        assertThat(actualTemplateVariables)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemplateVariables);

        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
        verifyNoMoreInteractions(authorService, genreService);
    }


    private static List<Author> getDbAuthors() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new Genre(Long.valueOf(id), "Genre_" + id))
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
