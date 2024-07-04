package ru.otus.hw.services.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.*;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SingleBookService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookRepository bookRepository;
    private final BookCommentService bookCommentService;


    public Map<String, Object> getTemplateVariablesEmptyBook() {

        Map<String, Object> templateVariables = new HashMap<>();

        List<BookEditAuthorDto> authorsFullList = authorService.findAll().stream()
                .map(BookEditAuthorDto::new)
                .toList();
        templateVariables.put("authors", authorsFullList);

        List<BookEditGenreDto> genresFullList = genreService.findAll().stream()
                .map(BookEditGenreDto::new)
                .toList();
        templateVariables.put("genres", genresFullList);

        return templateVariables;
    }

    public void processCreateBook(Book book) {
        bookRepository.save(book);
    }


    public Map<String, Object> getTemplateVariablesEditBookForm(long bookId) {

        Map<String, Object> templateVariables = new HashMap<>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        templateVariables.put("book", book);

        List<AuthorDto> authorsFullList = authorService.findAll();
        List<BookEditAuthorDto> authorsForViewList = prepareAuthorsList(book, authorsFullList);
        templateVariables.put("authors", authorsForViewList);

        List<GenreDto> genresFullList = genreService.findAll();
        List<BookEditGenreDto> genresForViewList = prepareGenresList(book, genresFullList);
        templateVariables.put("genres", genresForViewList);

        return templateVariables;
    }


    public Map<String, Object> getTemplateVariablesViewBook(long bookId) {

        Map<String, Object> templateVariables = new HashMap<>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        templateVariables.put("book", book);

        List<BookCommentDto> bookCommentsList = bookCommentService.findAllByBookId(bookId);
        templateVariables.put("book_comments", bookCommentsList);

        return templateVariables;
    }

    public void processUpdateBook(Book book) {
        bookRepository.save(book);
    }

    public Map<String, Object> getTemplateVariablesDeleteBook(long bookId) {

        Map<String, Object> viewEntities = new HashMap<>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        viewEntities.put("book", book);

        viewEntities.put("authors_list", book.getAuthors());

        viewEntities.put("genre", book.getGenre());

        return viewEntities;
    }

    public void processDeleteBook(long bookId) {

        bookRepository.deleteById(bookId);
    }

    private List<BookEditAuthorDto> prepareAuthorsList(BookDto book,
                                                       List<AuthorDto> authorsList) {

        List<Long> authorIdsList = book.getAuthors().stream()
                .map(AuthorDto::getId)
                .toList();

        List<BookEditAuthorDto> bookEditAuthorDto = authorsList.stream()
                .map(BookEditAuthorDto::new)
                .toList();

        bookEditAuthorDto.forEach(authorDto -> authorDto.setSelected(
                authorIdsList.contains(authorDto.getId())
        ));

        return bookEditAuthorDto;
    }

    private List<BookEditGenreDto> prepareGenresList(BookDto book,
                                                     List<GenreDto> genresList) {
        List<BookEditGenreDto> bookEditGenreList = genresList.stream()
                .map(BookEditGenreDto::new)
                .toList();

        bookEditGenreList.forEach(genreDto -> genreDto.setSelected(
                genreDto.getId() == book.getGenre().getId()
        ));

        return bookEditGenreList;
    }

}
