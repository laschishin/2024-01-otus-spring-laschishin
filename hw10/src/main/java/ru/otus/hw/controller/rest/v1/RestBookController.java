package ru.otus.hw.controller.rest.v1;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookRequest;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookMapperService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestBookController {

    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final BookRepository bookRepository;
    private final BookMapperService bookMapperService;

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

    @PatchMapping("api/v1/books/{bookId}")
    public BookDto updateBook(@PathVariable long bookId,
                              @RequestBody UpdateBookRequest request) throws JsonMappingException {
//                              @RequestBody Map<String, Object> request) {

//        bookRepository.findById(bookId)
//                .map(book -> {
//                    book.setTitle(request.getTitle());
//
//                })

        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);

        BookDto updatedBook = bookMapperService.map(bookDto, request);

        bookRepository.save(updatedBook.toDomainObject());

        return updatedBook;

    }



//    ResponseEntity

}
