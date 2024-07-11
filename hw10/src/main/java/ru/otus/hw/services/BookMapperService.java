package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BookMapperService {

    private final AuthorService authorService;
    private final GenreService genreService;

    public BookDto mapChanged(@NonNull BookDto dto,
                              @NonNull UpdateBookRequest request) {

        if (request.getTitle() != null) {
            dto.setTitle(request.getTitle());
        }

        if (request.getAuthorsIds() != null && !request.getAuthorsIds().isEmpty()) {
            Set<Long> authorIds = new HashSet<>(request.getAuthorsIds());
            List<AuthorDto> authors = authorService.findAllByIdIn(authorIds);
            dto.setAuthors(authors);
        }

        if (request.getGenreId() != null) {
            GenreDto genre = genreService.findById(request.getGenreId())
                    .orElseThrow(EntityNotFoundException::new);
            dto.setGenre(genre);
        }

        return dto;
    }

}
