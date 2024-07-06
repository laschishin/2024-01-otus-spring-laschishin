package ru.otus.hw.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.web.BookController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.web.ListBooksService;
import ru.otus.hw.services.web.SingleBookService;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@MockBean(value = {
        BookService.class
})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ListBooksService listBooksService;
    @MockBean
    SingleBookService singleBookService;


    @Test
    void listBooksTest() throws Exception {

        when(listBooksService.getTemplateVariablesListAllBooks()).thenReturn(
                Map.of("books", List.of()));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books_list"))
                .andExpect(model().attributeExists("books"));

        verify(listBooksService, times(1)).getTemplateVariablesListAllBooks();
        verifyNoMoreInteractions(listBooksService);
    }

    @Test
    void createBookFormTest() throws Exception {

        when(singleBookService.getTemplateVariablesEmptyBook()).thenReturn(
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

        verify(singleBookService, times(1)).getTemplateVariablesEmptyBook();
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void createBookPostTest() throws Exception {

        mvc.perform(post("/book/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(singleBookService, times(1)).processUpdateBook(new Book());
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void editBookFormTest() throws Exception {

        long bookId = 1L;

        when(singleBookService.getTemplateVariablesEditBookForm(bookId)).thenReturn(
                Map.of(
                        "book", new BookDto(),
                        "authors", List.of(),
                        "genres", List.of()
                )
        );

        mvc.perform(get("/book/edit/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("book_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));

        verify(singleBookService, times(1)).getTemplateVariablesEditBookForm(bookId);
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void editBookPostTest() throws Exception {

        long bookId = 1L;

        mvc.perform(post("/book/edit/{bookId}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(singleBookService, times(1)).processUpdateBook(new Book());
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void viewBookTest() throws Exception {

        BookDto book = new BookDto(
                1, "Title", List.of(), new GenreDto(1, "Title"));
        long bookId = book.getId();

        when(singleBookService.getTemplateVariablesViewBook(bookId)).thenReturn(
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

        verify(singleBookService, times(1)).getTemplateVariablesViewBook(bookId);
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void confirmDeleteBookTest() throws Exception {


        BookDto book = new BookDto(
                1, "Title", List.of(), new GenreDto(1, "Title"));
        long bookId = book.getId();

        when(singleBookService.getTemplateVariablesDeleteBook(bookId)).thenReturn(
                Map.of(
                        "book", book,
                        "authors", List.of(),
                        "genre", new Genre()
                )
        );

        mvc.perform(get("/book/delete/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("book_delete_confirm"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genre"));

        verify(singleBookService, times(1)).getTemplateVariablesDeleteBook(bookId);
        verifyNoMoreInteractions(singleBookService);
    }

    @Test
    void deleteBookPostTest() throws Exception {

        long bookId = 1L;

        mvc.perform(post("/book/delete/{bookId}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(singleBookService, times(1)).processDeleteBook(bookId);
        verifyNoMoreInteractions(singleBookService);
    }

}
