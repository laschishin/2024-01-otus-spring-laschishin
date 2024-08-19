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
public class BookCreateService {

    private final BookService bookService;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;
    private final GenreRepository genreRepository;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;


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

    public void processCreateBook(@NotNull Long bookId,
                                  @NotNull String bookTitle,
                                  @NotNull List<Long> authorsList,
                                  @NotNull Long genreId) {

        if(bookTitle.isEmpty()) {
            throw new BadArgumentsException("Book title should not be empty");
        }
        if(authorsList.isEmpty()) {
            throw new BadArgumentsException("Author(s) should not be empty");
        }
        List<Author> authors = new ArrayList<>();
        authorsList.forEach(authorId ->
                authors.add(
                        authorRepository.findById(authorId)
                                .orElseThrow(() -> new EntityNotFoundException("Author with id = %d doesn't exists".formatted(authorId)))
                )
        );

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id = %d doesn't found".formatted(genreId)));

        Book book = new Book();
        book.setTitle(bookTitle);
        book.setAuthors(authors);
        book.setGenre(genre);

        bookRepository.save(book);
    }

}