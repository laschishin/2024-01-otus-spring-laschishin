package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.web.ListGenresService;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final ListGenresService listGenresService;

    @GetMapping("/genres")
    public String listGenres(Model model) {

        model.addAllAttributes(
                listGenresService.listGenresGetAttributes());

        return "genres_list";
    }

}
