package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateBookRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("authors")
    private List<Long> authorsIds;

    @JsonProperty("genre")
    private Long genreId;

}
