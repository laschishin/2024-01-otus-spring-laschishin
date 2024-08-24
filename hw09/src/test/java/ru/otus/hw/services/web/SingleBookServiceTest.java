//package ru.otus.hw.services.web;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import ru.otus.hw.dto.*;
//import ru.otus.hw.models.Author;
//import ru.otus.hw.models.Book;
//import ru.otus.hw.models.BookComment;
//import ru.otus.hw.models.Genre;
//import ru.otus.hw.repositories.BookRepository;
//import ru.otus.hw.services.AuthorService;
//import ru.otus.hw.services.BookCommentService;
//import ru.otus.hw.services.BookService;
//import ru.otus.hw.services.GenreService;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchException;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@MockBean(value = {
//        AuthorService.class
//})
//class SingleBookServiceTest {
//
//    @Autowired
//    BookCreateService bookCreateService;
//
//    @MockBean
//    AuthorService authorService;
//    @MockBean
//    GenreService genreService;
//    @MockBean
//    BookService bookService;
//    @MockBean
//    BookCommentService bookCommentService;
//    @MockBean
//    BookRepository bookRepository;
//
//
//    private List<Author> dbAuthors;
//    private List<Genre> dbGenres;
//    private List<Book> dbBooks;
//    private List<BookComment> dbBookComments;
//
//    @BeforeEach
//    void setUp() {
//        dbAuthors = getDbAuthors();
//        dbGenres = getDbGenres();
//        dbBooks = getDbBooks();
//        dbBookComments = getDbBookComments();
//    }
//
//
//    @Test
//    void getTemplateVariablesEmptyBookTest() {
//        List<AuthorDto> authorsList = dbAuthors.stream()
//                .map(AuthorDto::new)
//                .toList();
//        List<BookEditAuthorDto> templateAuthorsList = authorsList.stream()
//                .map(BookEditAuthorDto::new)
//                .toList();
//
//        List<GenreDto> genresList = dbGenres.stream()
//                .map(GenreDto::new)
//                .toList();
//        List<BookEditGenreDto> templateGenresList = genresList.stream()
//                .map(BookEditGenreDto::new)
//                .toList();
//
//        Map<String, Object> expectedTemplateVariables = Map.of(
//                "authors", templateAuthorsList,
//                "genres", templateGenresList
//        );
//
//        when(authorService.findAll()).thenReturn(authorsList);
//        when(genreService.findAll()).thenReturn(genresList);
//
//        Map<String, Object> actualTemplateVariables = bookCreateService.getTemplateVariablesEmptyBook();
//
//        assertThat(actualTemplateVariables)
//                .usingRecursiveComparison()
//                .isEqualTo(expectedTemplateVariables);
//
//        verify(authorService, times(1)).findAll();
//        verify(genreService, times(1)).findAll();
//        verifyNoMoreInteractions(authorService, genreService);
//    }
//
//
//
////    @Test
////    void processUpdateBook_When_BookDoesNotExists() {
////
////        Book nonExistingBook = new Book();
////        nonExistingBook.setId(100500L);
////
////        EntityNotFoundException expectedException = new EntityNotFoundException("Book with id = %d doesn't exists".formatted(nonExistingBook.getId()));
////
////        Throwable actualException = catchException(() -> singleBookService.processUpdateBook(nonExistingBook));
////
////        assertThat(actualException)
////                .isInstanceOf(expectedException.getClass())
////                .hasMessage(expectedException.getMessage());
////
////        verifyNoMoreInteractions(bookRepository);
////    }
//
////    @Test
////    void getTemplateVariablesEditBookFormTest() {
////
////        BookDto book = new BookDto(dbBooks.get(0));
////        long bookId = book.getId();
////
////        List<AuthorDto> authorsList = dbAuthors.stream()
////                .map(AuthorDto::new)
////                .toList();
////        List<BookEditAuthorDto> templateAuthorsList = authorsList.stream()
////                .map(BookEditAuthorDto::new)
////                .toList();
////        templateAuthorsList.get(0).setSelected(true);
////
////        List<GenreDto> genresList = dbGenres.stream()
////                .map(GenreDto::new)
////                .toList();
////        List<BookEditGenreDto> templateGenresList = genresList.stream()
////                .map(BookEditGenreDto::new)
////                .toList();
////        templateGenresList.get(0).setSelected(true);
////
////        Map<String, Object> expectedTemplateVariables = Map.of(
////                "book", book,
////                "authors", templateAuthorsList,
////                "genres", templateGenresList
////        );
////
////        when(bookService.findById(bookId)).thenReturn(Optional.of(book));
////        when(authorService.findAll()).thenReturn(authorsList);
////        when(genreService.findAll()).thenReturn(genresList);
////
////        Map<String, Object> actualTemplateVariables = singleBookService.getTemplateVariables(bookId);
////
////        assertThat(actualTemplateVariables)
////                .usingRecursiveComparison()
////                .isEqualTo(expectedTemplateVariables);
////
////        verify(bookService, times(1)).findById(bookId);
////        verify(authorService, times(1)).findAll();
////        verify(genreService, times(1)).findAll();
////        verifyNoMoreInteractions(bookService, authorService, genreService);
////    }
//
//
////    @Test
////    void getTemplateVariablesViewBookTest() {
////
////        BookDto book = new BookDto(dbBooks.get(0));
////        long bookId = book.getId();
////
////        List<BookCommentDto> bookCommentsList = dbBookComments.stream()
////                .map(BookCommentDto::new)
////                .toList();
////
////
////        Map<String, Object> expectedTemplateVariables = Map.of(
////                "book", book,
////                "book_comments", bookCommentsList
////        );
////
////        when(bookService.findById(bookId)).thenReturn(Optional.of(book));
////        when(bookCommentService.findAllByBookId(bookId)).thenReturn(bookCommentsList);
////
////        Map<String, Object> actualTemplateVariables = singleBookService.getTemplateVariablesViewBook(bookId);
////
////        assertThat(actualTemplateVariables)
////                .usingRecursiveComparison()
////                .isEqualTo(expectedTemplateVariables);
////
////        verify(bookService, times(1)).findById(bookId);
////        verify(bookCommentService, times(1)).findAllByBookId(bookId);
////        verifyNoMoreInteractions(bookService, bookCommentService);
////    }
//
//
//
//    private static List<Author> getDbAuthors() {
//        return IntStream.range(1, 7).boxed()
//                .map(id -> new Author(id, "Author_" + id))
//                .toList();
//    }
//
//    private static List<Genre> getDbGenres() {
//        return IntStream.range(1, 7).boxed()
//                .map(id -> new Genre(id, "Genre_" + id))
//                .toList();
//    }
//
//    private static List<Book> getDbBooks() {
//        return IntStream.range(1, 7).boxed()
//                .map(id -> new Book(id, "Title_" + id, List.of(getDbAuthors().get(id - 1)), getDbGenres().get(id - 1)))
//                .toList();
//    }
//
//    private static List<BookComment> getDbBookComments() {
//        return IntStream.range(1, 7).boxed()
//                .map(id -> new BookComment(id, getDbBooks().get(id - 1), "Comment_" + id))
//                .toList();
//    }
//
//}
