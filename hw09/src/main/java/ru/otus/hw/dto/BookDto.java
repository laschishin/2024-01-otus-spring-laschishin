package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;
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

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authors = AuthorDto.toDto(book.getAuthors());
        this.genre = new GenreDto(book.getGenre());
    }

    public String getAuthorsFullNames() {
        return authors.stream()
                .map(AuthorDto::getFullName)
                .collect(Collectors.joining(", "));
    }

    public List<Long> getAuthorIds() {
        return authors.stream()
                .map(AuthorDto::getId)
                .collect(Collectors.toList());
    }

}
