package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Genre;

@Getter
@Setter
@AllArgsConstructor
public class BookEditGenreDto {

    private Long id;

    private String name;

    private boolean isSelected;

    public BookEditGenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.isSelected = false;
    }

    public BookEditGenreDto(GenreDto genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.isSelected = false;
    }

}
