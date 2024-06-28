package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.web.ListAuthorsService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final ListAuthorsService listAuthorsService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {

        model.addAllAttributes(
                listAuthorsService.listAuthorsGetAttributes());

        return "authors_list";
    }

}
