package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.web.BookCreateService;
import ru.otus.hw.services.web.BookDeleteService;
import ru.otus.hw.services.web.BookUpdateService;
import ru.otus.hw.services.web.BookViewService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookViewService bookViewService;
    private final BookUpdateService bookUpdateService;
    private final BookCreateService bookCreateService;
    private final BookDeleteService bookDeleteService;


    @GetMapping("/")
    public String listBooks(Model model) {

        model.addAllAttributes(
                bookViewService.getTemplateVariablesListAllBooks()
        );

        return "books_list";
    }

    @GetMapping("/book/create")
    public String createBookForm(Model model) {

        Map<String, Object> templateVariables = bookCreateService.getTemplateVariablesEmptyBook();

        model.addAttribute("authors", templateVariables.get("authors"));
        model.addAttribute("genres", templateVariables.get("genres"));

        return "book_create";
    }

    @PostMapping("/book/create")
    public String createBookPost(Book book) {

        bookCreateService.processCreateBook(book);

        return "redirect:/";
    }

    @GetMapping("/book/edit/{bookId}")
    public String editBookGet(@PathVariable long bookId, Model model) {

        model.addAllAttributes(
                bookUpdateService.getTemplateVariables(bookId)
        );

        return "book_edit";
    }

//    @PostMapping("/book/edit/{bookId}")
//    public String editBookPost(@PathVariable Long bookId, Book book) {
//
//        bookUpdateService.processUpdateBook(bookId, book);
//
//        return "redirect:/";
//    }

    @PostMapping("/book/edit/{bookId}")
    public String editBookPost(@PathVariable Long bookId,
                               @RequestParam("title") String bookTitle,
                               @RequestParam("authors") Long[] authorsList,
                               @RequestParam("genre") Long genreId) {

        bookUpdateService.processUpdateBook(bookId, bookTitle, List.of(authorsList), genreId);

        return "redirect:/";
    }

    @GetMapping("/book/view/{bookId}")
    public String viewBook(@PathVariable long bookId, Model model) {

        Map<String, Object> templateVariables = bookViewService.getTemplateVariablesViewBook(bookId);

        model.addAttribute("book", templateVariables.get("book"));
        model.addAttribute("book_comments", templateVariables.get("book_comments"));

        return "book_view";
    }

    @GetMapping("/book/delete/{bookId}")
    public String confirmDeleteBook(@PathVariable long bookId, Model model) {

        Map<String, Object> templateVariables = bookDeleteService.getTemplateVariablesDeleteBook(bookId);

        model.addAttribute("book", templateVariables.get("book"));
        model.addAttribute("authors_list", templateVariables.get("authors_list"));
        model.addAttribute("genre", templateVariables.get("genre"));

        return "book_delete_confirm";
    }

    @PostMapping("/book/delete/{bookId}")
    public String deleteBookPost(@PathVariable long bookId) {

        bookDeleteService.processDeleteBook(bookId);

        return "redirect:/";
    }

}
