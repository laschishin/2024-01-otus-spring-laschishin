package ru.otus.hw.services.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListGenresService {

    private final GenreService genreService;

    public Map<String, Object> listGenresGetAttributes() {

        Map<String, Object> viewEntities = new HashMap<>();

        List<GenreDto> genres = genreService.findAll();

        viewEntities.put("genres", genres);

        return viewEntities;

    }

}
