package ru.otus.hw.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.web.ListBooksService;
import ru.otus.hw.services.web.SingleBookService;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@MockBean(value = {
        SingleBookService.class
})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ListBooksService listBooksService;
    @MockBean
    SingleBookService singleBookService;

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
    void listBooksTest() throws Exception {

        List<BookDto> bookList = getDbBooks().stream()
                .map(BookDto::new)
                .toList();

        Map<String, Object> expectedModelAttributes = Map.of("books", bookList);

        when(listBooksService.listBooksGetAttributes()).thenReturn(expectedModelAttributes);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books_list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookList));

        verify(listBooksService, times(1)).listBooksGetAttributes();
        verifyNoMoreInteractions(listBooksService);
    }

    @Test
    void CreateBookTest() throws Exception {

        List<BookDto> bookList = getDbBooks().stream()
                .map(BookDto::new)
                .toList();

        Map<String, Object> expectedModelAttributes = Map.of("books", bookList);

        when(singleBookService.createBookGetAttributes()).thenReturn(expectedModelAttributes);

        mvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_edit"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookList));

        verify(listBooksService, times(1)).listBooksGetAttributes();
        verifyNoMoreInteractions(listBooksService);
    }

    @Test
    void updateBookTest() throws Exception {

        mvc.perform(post("/book/edit/{bookId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
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