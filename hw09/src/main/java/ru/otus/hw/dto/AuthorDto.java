package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AuthorDto {

    private Long id;

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
