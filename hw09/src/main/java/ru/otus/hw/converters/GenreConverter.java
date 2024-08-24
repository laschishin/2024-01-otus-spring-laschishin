package ru.otus.hw.converters;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

@Component
@AllArgsConstructor
public class GenreConverter implements Converter<String, Genre> {

    private final GenreRepository genreRepository;

//    public String genreToString(Genre genre) {
//        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
//    }


    @Override
    public Genre convert(String source) {

        Long id = Long.parseLong(source);

        Genre genre = genreRepository.getReferenceById(id);

        return genre;

    }
}
