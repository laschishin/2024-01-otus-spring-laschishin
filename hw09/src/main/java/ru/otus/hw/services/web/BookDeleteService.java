package ru.otus.hw.services.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditAuthorDto;
import ru.otus.hw.dto.BookEditGenreDto;
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
public class BookDeleteService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;


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

}