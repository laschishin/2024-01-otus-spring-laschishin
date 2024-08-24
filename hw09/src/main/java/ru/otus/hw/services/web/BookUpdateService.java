package ru.otus.hw.services.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.*;
import ru.otus.hw.exceptions.BadArgumentsException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookUpdateService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final GenreService genreService;
    private final GenreRepository genreRepository;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;


    public Map<String, Object> getTemplateVariables(long bookId) {

        Map<String, Object> templateVariables = new HashMap<String, Object>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = %d doesn't exists".formatted(bookId)));
        templateVariables.put("book", book);

        List<AuthorDto> authorsFullList = authorService.findAll();
        List<BookEditAuthorDto> authorsForViewList = prepareAuthorsList(book, authorsFullList);
        templateVariables.put("authors", authorsForViewList);

        List<GenreDto> genresFullList = genreService.findAll();
        List<BookEditGenreDto> genresForViewList = prepareGenresList(book, genresFullList);
        templateVariables.put("genres", genresForViewList);

        return templateVariables;
    }

    public void processUpdateBook(@NotNull Long bookId,
                                  @NotNull String bookTitle,
                                  @NotNull List<Long> authorsList,
                                  @NotNull Long genreId) {

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = %d doesn't exists".formatted(bookId)))
                .toDomainObject();

        book.setTitle(bookTitle);

        List<Author> authors = new ArrayList<>();
        authorsList.forEach(authorId ->
                authors.add(
                        authorRepository.findById(authorId)
                                .orElseThrow(() -> new EntityNotFoundException("Author with id = %d doesn't exists".formatted(authorId)))
                )
        );
//        for (Long authorId : authorsList) {
//            authors.add(
//                    authorRepository.findById(authorId)
//                            .orElseThrow(() -> new EntityNotFoundException("Author with id = %d doesn't exists".formatted(authorId)))
//            );
//        }
        book.setAuthors(authors);

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id = %d doesn't found".formatted(genreId)));
        book.setGenre(genre);

        bookRepository.save(book);

    }

    public void processUpdateBook(long bookId, Book book) {

        validateUpdateBook(bookId, book);

        Book repoBook = bookService.findById(book.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id = %d doesn't exists".formatted(book.getId())))
                .toDomainObject();

        repoBook.setTitle(book.getTitle());
        repoBook.setAuthors(book.getAuthors());
        repoBook.setGenre(book.getGenre());

        bookRepository.save(repoBook);
    }

    public void validateUpdateBook(long bookId, Book book) {

        if (bookId != book.getId()) {
            throw new BadArgumentsException("Wrong book data for book_id %d".formatted(bookId));
        }

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