package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.dto.*;
import ru.otus.hw.exceptions.BadArgumentsException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.web.BookCreateService;
import ru.otus.hw.services.web.BookDeleteService;
import ru.otus.hw.services.web.BookUpdateService;
import ru.otus.hw.services.web.BookViewService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@MockBean(value = {
        BookService.class,
        AuthorRepository.class
})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookViewService bookViewService;
    @MockBean
    BookCreateService bookCreateService;
    @MockBean
    BookUpdateService bookUpdateService;
    @MockBean
    BookDeleteService bookDeleteService;
    @MockBean
    AuthorRepository authorRepository;
    @MockBean
    GenreRepository genreRepository;


    //    private List<Author> mockAuthors;
    private List<AuthorDto> mockAuthorsDto;
    //    private List<Genre> mockGenres;
    private List<GenreDto> mockGenresDto;
    private List<Book> mockBooks;
    private List<BookDto> mockBooksDto;


    @BeforeEach
    void setUp() {
//        mockAuthors = MockedEntities.getAuthors();
        mockAuthorsDto = MockedEntities.getAuthorsDto();
//        mockGenres = MockedEntities.getGenres();
        mockGenresDto = MockedEntities.getGenresDto();
        mockBooks = MockedEntities.getBooks();
        mockBooksDto = MockedEntities.getBooksDto();
    }

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    void listBooksTest() throws Exception {

        when(bookViewService.getTemplateVariablesListAllBooks()).thenReturn(
                Map.of("books", List.of()));

        mvc.perform(get("/")).andExpectAll(
                status().isOk(),
                view().name("books_list"),
                model().attributeExists("books")
        );

        verify(bookViewService, times(1)).getTemplateVariablesListAllBooks();
        verifyNoMoreInteractions(bookViewService);
    }

    @Test
    void createBookFormTest() throws Exception {

        when(bookCreateService.getTemplateVariablesEmptyBook()).thenReturn(
                Map.of(
                        "authors", List.of(),
                        "genres", List.of()
                )
        );

        mvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_create"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));

        verify(bookCreateService, times(1)).getTemplateVariablesEmptyBook();
        verifyNoMoreInteractions(bookCreateService);
    }

    @Test
    void createBookPostTest() throws Exception {

        mvc.perform(post("/book/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookCreateService, times(1)).processCreateBook(new Book());
        verifyNoMoreInteractions(bookCreateService);
    }

    @Test
    void editBookGet_When_BookNotFoundTest() throws Exception {

        long bookId = 100500L;

//        EntityNotFoundException expectedException = Assertions.assertThrows(
//                EntityNotFoundException.class,
//                () -> bookUpdateService.getTemplateVariables(bookId),
//                "Book with id = %d doesn't exists".formatted(bookId)
//        );

        when(bookUpdateService.getTemplateVariables(bookId))
                .thenThrow(new EntityNotFoundException("Book with id = %d doesn't exists".formatted(bookId)));


        mvc.perform(get("/book/edit/{bookId}", bookId)).andExpectAll(
                status().isBadRequest()
        );

        verify(bookUpdateService, times(1)).getTemplateVariables(bookId);
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void editBookGetTest() throws Exception {

        BookDto book = mockBooksDto.get(0);
        long bookId = book.getId();

        List<BookEditAuthorDto> authors = mockAuthorsDto.stream()
                .map(BookEditAuthorDto::new)
                .toList();
        authors.get(0).setSelected(true);
        authors.get(2).setSelected(true);

        List<BookEditGenreDto> genres = mockGenresDto.stream()
                .map(BookEditGenreDto::new)
                .toList();
        genres.get(0).setSelected(true);

        when(bookUpdateService.getTemplateVariables(bookId)).thenReturn(
                Map.of(
                        "book", book,
                        "authors", authors,
                        "genres", genres
                )
        );

        mvc.perform(get("/book/edit/{bookId}", bookId)).andExpectAll(
                status().isOk(),
                view().name("book_edit"),
                model().attribute("book", book),
                model().attribute("authors", authors),
                model().attribute("genres", genres)
        );

        verify(bookUpdateService, times(1)).getTemplateVariables(bookId);
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void editBookPost_When_BookDoesNotExists() throws Exception {

        Book book = mockBooks.get(0);
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();

        BadArgumentsException expectedException = new BadArgumentsException(
                "Book with id = %d doesn't exists".formatted(book.getId()));

        doThrow(expectedException)
                .when(bookUpdateService).processUpdateBook(
                        book.getId(),
                        book.getTitle(),
                        authorIds,
                        book.getGenre().getId()
                );

        Map<String, List<String>> params = Map.of(
                "id", List.of(book.getId().toString()),
                "title", List.of(book.getTitle()),
                "authors", book.getAuthors().stream().map(a -> a.getId().toString()).toList(),
                "genre", List.of(book.getGenre().getId().toString())
        );

        mvc.perform(post("/book/edit/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(new LinkedMultiValueMap<>(params))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        result -> {
                            assertThat(result.getResolvedException())
                                    .isInstanceOf(expectedException.getClass());
                            assertThat(Objects.requireNonNullElse(result.getResolvedException(), new Exception()).getMessage())
                                    .isEqualTo(expectedException.getMessage());
                        }
                );

        verify(bookUpdateService, times(1)).processUpdateBook(
                book.getId(),
                book.getTitle(),
                authorIds,
                book.getGenre().getId()
        );
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void editBookPost_When_AuthorDoesNotExists() throws Exception {

        Book book = mockBooks.get(0);
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();

        BadArgumentsException expectedException = new BadArgumentsException(
                "Author with id = %d doesn't exists".formatted(book.getAuthors().get(0).getId()));

        doThrow(expectedException).when(bookUpdateService)
                .processUpdateBook(
                        book.getId(),
                        book.getTitle(),
                        authorIds,
                        book.getGenre().getId()
                );

        Map<String, List<String>> params = Map.of(
                "id", List.of(book.getId().toString()),
                "title", List.of(book.getTitle()),
                "authors", book.getAuthors().stream().map(a -> a.getId().toString()).toList(),
                "genre", List.of(book.getGenre().getId().toString())
        );

        mvc.perform(post("/book/edit/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(new LinkedMultiValueMap<>(params))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        result -> {
                            assertThat(result.getResolvedException())
                                    .isInstanceOf(expectedException.getClass());
                            assertThat(Objects.requireNonNullElse(result.getResolvedException(), new Exception()).getMessage())
                                    .isEqualTo(expectedException.getMessage());
                        }
                );

        verify(bookUpdateService, times(1)).processUpdateBook(
                book.getId(),
                book.getTitle(),
                authorIds,
                book.getGenre().getId()
        );
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void editBookPost_When_GenreDoesNotExists() throws Exception {

        Book book = mockBooks.get(0);
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();

        BadArgumentsException expectedException = new BadArgumentsException(
                "Genre with id = %d doesn't exists".formatted(book.getGenre().getId()));

        doThrow(expectedException).when(bookUpdateService)
                .processUpdateBook(
                        book.getId(),
                        book.getTitle(),
                        authorIds,
                        book.getGenre().getId()
                );

        Map<String, List<String>> params = Map.of(
                "id", List.of(book.getId().toString()),
                "title", List.of(book.getTitle()),
                "authors", book.getAuthors().stream().map(a -> a.getId().toString()).toList(),
                "genre", List.of(book.getGenre().getId().toString())
        );

        mvc.perform(post("/book/edit/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(new LinkedMultiValueMap<>(params))
                )
                .andExpectAll(
                        status().isBadRequest(),
                        result -> {
                            assertThat(result.getResolvedException())
                                    .isInstanceOf(expectedException.getClass());
                            assertThat(Objects.requireNonNullElse(result.getResolvedException(), new Exception()).getMessage())
                                    .isEqualTo(expectedException.getMessage());
                        }
                );

        verify(bookUpdateService, times(1)).processUpdateBook(
                book.getId(),
                book.getTitle(),
                authorIds,
                book.getGenre().getId()
        );
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void editBookPostTest() throws Exception {

        Book book = mockBooks.get(0);
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();

        Map<String, List<String>> params = Map.of(
                "id", List.of(book.getId().toString()),
                "title", List.of(book.getTitle()),
                "authors", book.getAuthors().stream().map(a -> a.getId().toString()).toList(),
                "genre", List.of(book.getGenre().getId().toString())
        );

//        doNothing().when(bookUpdateService)
//                .processUpdateBook(
//                        book.getId(),
//                        book.getTitle(),
//                        authorIds,
//                        book.getGenre().getId()
//                );

        mvc.perform(post("/book/edit/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(new LinkedMultiValueMap<>(params)))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/")
                );

        verify(bookUpdateService, times(1)).processUpdateBook(
                book.getId(),
                book.getTitle(),
                authorIds,
                book.getGenre().getId()
        );
        verifyNoMoreInteractions(bookUpdateService);
    }

    @Test
    void viewBookTest() throws Exception {

        BookDto book = new BookDto(
                1L, "Title", List.of(), new GenreDto(1L, "Title"));
        long bookId = book.getId();

        when(bookViewService.getTemplateVariablesViewBook(bookId)).thenReturn(
                Map.of(
                        "book", book,
                        "book_comments", List.of()
                )
        );

        mvc.perform(get("/book/view/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("book_view"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("book_comments"));

        verify(bookViewService, times(1)).getTemplateVariablesViewBook(bookId);
        verifyNoMoreInteractions(bookViewService);
    }

    @Test
    void confirmDeleteBookTest() throws Exception {

        BookDto book = new BookDto(
                1L, "Title", List.of(), new GenreDto(1L, "Title"));
        long bookId = book.getId();

        when(bookDeleteService.getTemplateVariablesDeleteBook(bookId)).thenReturn(
                Map.of(
                        "book", book,
                        "authors_list", List.of(),
                        "genre", new Genre()
                )
        );

        mvc.perform(get("/book/delete/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("book_delete_confirm"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors_list"))
                .andExpect(model().attributeExists("genre"));

        verify(bookDeleteService, times(1)).getTemplateVariablesDeleteBook(bookId);
        verifyNoMoreInteractions(bookDeleteService);
    }

    @Test
    void deleteBookPostTest() throws Exception {

        long bookId = 1L;

        mvc.perform(post("/book/delete/{bookId}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookDeleteService, times(1)).processDeleteBook(bookId);
        verifyNoMoreInteractions(bookDeleteService);
    }


    private static class MockedEntities {

        public static List<Genre> getGenres() {
            return LongStream.range(1L, 7L).boxed()
                    .map(id -> new Genre(id, "Genre_" + id))
                    .toList();
        }

        public static List<GenreDto> getGenresDto() {
            return getGenres().stream()
                    .map(GenreDto::new)
                    .toList();
        }

        public static List<Author> getAuthors() {
            return LongStream.range(1L, 7L).boxed()
                    .map(id -> new Author(id, "Author_" + id))
                    .toList();
        }

        public static List<AuthorDto> getAuthorsDto() {
            return getAuthors().stream()
                    .map(AuthorDto::new)
                    .toList();
        }

        public static List<Book> getBooks() {
            return IntStream.range(1, 7).boxed()
                    .map(id -> new Book(Long.valueOf(id), "Title_" + id, List.of(getAuthors().get(id - 1)), getGenres().get(id - 1)))
                    .toList();
        }

        public static List<BookDto> getBooksDto() {
            return getBooks().stream()
                    .map(BookDto::new)
                    .toList();
        }
    }
}
