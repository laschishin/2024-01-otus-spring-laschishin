package ru.otus.hw.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;


    @GetMapping("/")
    public String listBooks(Model model){
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books_list";
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") long Id, Model model) {

        BookDto book = bookService.findById(Id)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", book);

        List<AuthorDto> authors = authorService.findAll().stream()
                .filter(author -> !book.getAuthorIds().contains(author.getId()))
                .toList();


    }

}
