package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateBookResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("authors")
    private List<AuthorDto> authors;

    @JsonProperty("genre")
    private GenreDto genre;

}
