package ru.otus.hw.dto;


import lombok.Data;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AuthorDto {

    private long id;

    private String fullName;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.fullName = author.getFullName();
    }

    public static List<AuthorDto> toDto(List<Author> authors) {
        return authors.stream()
                .map(AuthorDto::new)
                .collect(Collectors.toList());
    }

    public Author toDomainObject() {
        return new Author(id, fullName);
    }

}
