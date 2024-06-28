package ru.otus.hw.services.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListAuthorsService {

    private final AuthorService authorService;

    public Map<String, Object> listAuthorsGetAttributes() {

        Map<String, Object> viewEntities = new HashMap<>();

        List<AuthorDto> authors = authorService.findAll();

        viewEntities.put("authors", authors);

        return viewEntities;

    }

}
