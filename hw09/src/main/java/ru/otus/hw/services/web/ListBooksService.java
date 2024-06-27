package ru.otus.hw.services.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListBooksService {

    private final BookService bookService;

    public Map<String, Object> listBooksGetAttributes() {

        Map<String, Object> viewEntities = new HashMap<>();

        List<BookDto> books = bookService.findAll();

        viewEntities.put("books", books);

        return viewEntities;

    }

}
