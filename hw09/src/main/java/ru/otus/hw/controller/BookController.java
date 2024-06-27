package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.web.ListBooksService;
import ru.otus.hw.services.web.SingleBookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final ListBooksService listBooksService;
    private final SingleBookService singleBookService;


    @GetMapping("/")
    public String listBooks(Model model) {

        model.addAllAttributes(
                listBooksService.listBooksGetAttributes());

        return "books_list";
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {

        model.addAllAttributes(
                singleBookService.createBookGetAttributes()
        );

        return "book_edit";

    }

    @GetMapping("/book/edit/{bookId}")
    public String editBook(@PathVariable long bookId, Model model) {

        model.addAllAttributes(
                singleBookService.editBookGetAttributes(bookId)
        );

        return "book_edit";
    }

    @PostMapping("/book/edit/{bookId}")
    public String updateBook(Book book, Model model) {

        singleBookService.processUpdateBook(book);

        model.addAllAttributes(
                listBooksService.listBooksGetAttributes()
        );

        return "redirect:/";

    }

    @GetMapping("/book/delete/{bookId}")
    public String confirmDeleteBook(@PathVariable long bookId, Model model) {


        model.addAllAttributes(
                singleBookService.deleteBookGetAttributes(bookId)
        );

        return "book_delete_confirm";
    }

    @PostMapping("/book/delete/{bookId}")
    public String deleteBook(@PathVariable long bookId, Model model) {

        singleBookService.processDeleteBook(bookId);

        model.addAllAttributes(
                listBooksService.listBooksGetAttributes()
        );

        return "redirect:/";
    }

}
