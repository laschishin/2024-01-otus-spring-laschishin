package ru.otus.hw.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditAuthorDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.book_edit.BookEditService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookEditService bookEditService;


    @GetMapping("/")
    public String listBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books_list";
    }

    @GetMapping("/book/edit/{Id}")
    public String editBook(@PathVariable long Id, Model model) {

        BookDto book = bookService.findById(Id)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", book);

        List<AuthorDto> authors = authorService.findAll().stream()
                .filter(author -> !book.getAuthorIds().contains(author.getId()))
                .toList();
        List<BookEditAuthorDto> authorsList = bookEditService.prepareAuthorsList(book, authors);
        model.addAttribute("authors_list", authorsList);

        return "book_edit";
    }

}
