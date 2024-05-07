package ru.otus.hw.dto;


import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
public class GenreDto {

    private long id;

    private String name;

    public Genre toDomainObject() {
        return new Genre(id, name);
    }

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

}
