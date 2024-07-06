package ru.otus.hw.controller.rest.v1;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestAuthorController {

    private final AuthorService authorService;

    @GetMapping("api/v1/authors")
    public List<AuthorDto> listAuthors() {
        return authorService.findAll();
    }

    @GetMapping("api/v1/authors/{authorId}")
    public AuthorDto getAuthor(@PathVariable long authorId) {
        return authorService.findById(authorId)
                .orElseThrow(EntityNotFoundException::new);
    }


}
