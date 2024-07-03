package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
public class GenreDto {

    private long id;

    private String name;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

    public Genre toDomainObject() {
        return new Genre(id, name);
    }

}
