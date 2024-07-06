package ru.otus.hw.controller.rest.v1;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestBookController {

    private final BookService bookService;
    private final BookCommentService bookCommentService;

    @GetMapping("api/v1/books")
    public List<BookDto> listBooks() {
        return bookService.findAll();
    }

    @GetMapping("api/v1/books/{bookId}")
    public BookDto getBook(@PathVariable long bookId) {
        return bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("api/v1/books/{bookId}/comments")
    public List<BookCommentDto> getBookComments(@PathVariable long bookId) {
        return bookCommentService.findAllByBookId(bookId);
    }

}
