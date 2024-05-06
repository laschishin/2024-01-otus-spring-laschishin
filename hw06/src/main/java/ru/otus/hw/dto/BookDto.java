package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private List<AuthorDto> authors;

    private GenreDto genre;

    public Book toDomainObject() {
        return new Book(
                id,
                title,
                authors.stream().map(AuthorDto::toDomainObject).collect(Collectors.toList()),
                genre.toDomainObject()
        );
    }

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthors()),
                GenreDto.toDto(book.getGenre())
        );
    }

}
