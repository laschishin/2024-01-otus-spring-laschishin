package ru.otus.hw.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEditGenreDto {

    private long id;

    private String name;

    private boolean isSelected;

    public BookEditGenreDto(GenreDto genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.isSelected = false;
    }

}
