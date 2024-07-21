package ru.otus.hw.services.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.*;
import ru.otus.hw.exceptions.BadArgumentsException;
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
public class BookViewService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;


    public Map<String, Object> getTemplateVariablesViewBook(long bookId) {

        Map<String, Object> templateVariables = new HashMap<String, Object>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        templateVariables.put("book", book);

        List<BookCommentDto> bookCommentsList = bookCommentService.findAllByBookId(bookId);
        templateVariables.put("book_comments", bookCommentsList);

        return templateVariables;
    }

    public Map<String, Object> getTemplateVariablesListAllBooks() {

        List<BookDto> books = bookService.findAll();

        return Map.of("books", books);

    }

}