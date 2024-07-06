package ru.otus.hw.controller.rest.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestBookController {

    private final BookRepository bookRepository;

    @GetMapping("api/v1/books")
    public List<BookDto> listBooks() {
        return null;
//        return bookRepository.findAll().stream()
//                .map(BookDto::new)
//                .toList();
    }

}
