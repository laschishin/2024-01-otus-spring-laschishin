package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.services.BookCommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentCommands {

    private final BookCommentService bookCommentService;

    private final BookCommentConverter bookCommentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findBookCommentById(long id) {
        return bookCommentService.findById(id)
                .map(BookCommentDto::toDomainObject)
                .map(bookCommentConverter::bookCommentToString)
                .orElse("Comment with id %d has not been found".formatted(id));

    }

    @ShellMethod(value = "Find all comments by book_id", key = "cbbid")
    public String findAllBookCommentsByBookId(long bookId) {
        return bookCommentService.findAllByBookId(bookId).stream()
                .map(BookCommentDto::toDomainObject)
                .map(bookCommentConverter::bookCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cbins")
    public String insertBookComment(long bookId, String text) {
        BookCommentDto savedBookComment = bookCommentService.insert(bookId, text);
        return bookCommentConverter.bookCommentToString(savedBookComment.toDomainObject());
    }

    @ShellMethod(value = "Update comment", key = "cbupd")
    public String updateBookComment(long id, String text) {
        BookCommentDto savedBookComment = bookCommentService.update(id, text);
        return bookCommentConverter.bookCommentToString(savedBookComment.toDomainObject());
    }

    @ShellMethod(value = "Delete comment by id", key = "cbdel")
    public void deleteBookComment(long id) {
        bookCommentService.deleteById(id);
    }

}
