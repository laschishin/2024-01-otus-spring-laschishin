package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AuthorDto {

    private long id;

    private String fullName;

    public Author toDomainObject() {
        return new Author(id, fullName);
    }

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }

    public static List<AuthorDto> toDto(List<Author> authors) {
        return authors.stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());

    }

}
