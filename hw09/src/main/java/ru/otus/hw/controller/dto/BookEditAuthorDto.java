package ru.otus.hw.controller.dto;


import lombok.Data;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BookEditAuthorDto {

    private long id;

    private String fullName;

    private String fullNameDashed;

    public BookEditAuthorDto(Author author) {
        this.id = author.getId();
        this.fullName = author.getFullName();
        this.fullNameDashed = author.getFullName().toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        
    }

    public static List<BookEditAuthorDto> toDto(List<Author> authors) {
        return authors.stream()
                .map(BookEditAuthorDto::new)
                .collect(Collectors.toList());
    }

}
