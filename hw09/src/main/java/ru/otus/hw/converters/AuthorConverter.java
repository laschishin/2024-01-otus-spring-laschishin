package ru.otus.hw.converters;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;

@Component
@AllArgsConstructor
public class AuthorConverter implements Converter<String, Author> {

    private final AuthorRepository authorRepository;

//    public String authorToString(Author author) {
//        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
//    }


    @Override
    public Author convert(String source) {

        Long id = Long.parseLong(source);

        Author target = authorRepository.getReferenceById(id);

        return target;
    }
}
