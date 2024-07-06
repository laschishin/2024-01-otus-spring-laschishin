package ru.otus.hw.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.web.ListBooksService;
import ru.otus.hw.services.web.SingleBookService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final ListBooksService listBooksService;
    private final SingleBookService singleBookService;


    @GetMapping("/")
    public String listBooks(Model model) {

        Map<String, Object> templateVariables = listBooksService.getTemplateVariablesListAllBooks();

        model.addAttribute("books", templateVariables.get("books"));

        return "books_list";
    }

    @GetMapping("/book/create")
    public String createBookForm(Model model) {

        Map<String, Object> templateVariables = singleBookService.getTemplateVariablesEmptyBook();

        model.addAttribute("authors", templateVariables.get("authors"));
        model.addAttribute("genres", templateVariables.get("genres"));

        return "book_create";
    }

    @PostMapping("/book/create")
    public String createBookPost(Book book) {

        singleBookService.processUpdateBook(book);

        return "redirect:/";
    }

    @GetMapping("/book/edit/{bookId}")
    public String editBookForm(@PathVariable long bookId, Model model) {

        Map<String, Object> templateVariables = singleBookService.getTemplateVariablesEditBookForm(bookId);

        model.addAttribute("book", templateVariables.get("book"));
        model.addAttribute("authors", templateVariables.get("authors"));
        model.addAttribute("genres", templateVariables.get("genres"));

        return "book_edit";
    }

    @PostMapping("/book/edit/{bookId}")
    public String editBookPost(Book book) {

        singleBookService.processUpdateBook(book);

        return "redirect:/";
    }

    @GetMapping("/book/view/{bookId}")
    public String viewBook(@PathVariable long bookId, Model model) {

        Map<String, Object> templateVariables = singleBookService.getTemplateVariablesViewBook(bookId);

        model.addAttribute("book", templateVariables.get("book"));
        model.addAttribute("book_comments", templateVariables.get("book_comments"));

        return "book_view";
    }

    @GetMapping("/book/delete/{bookId}")
    public String confirmDeleteBook(@PathVariable long bookId, Model model) {

        Map<String, Object> templateVariables = singleBookService.getTemplateVariablesDeleteBook(bookId);

        model.addAttribute("book", templateVariables.get("book"));
        model.addAttribute("authors", templateVariables.get("authors"));
        model.addAttribute("genre", templateVariables.get("genre"));

        return "book_delete_confirm";
    }

    @PostMapping("/book/delete/{bookId}")
    public String deleteBookPost(@PathVariable long bookId) {

        singleBookService.processDeleteBook(bookId);

        return "redirect:/";
    }

}
