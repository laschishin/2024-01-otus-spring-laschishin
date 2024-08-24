package ru.otus.hw.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEditAuthorDto {

    private Long id;

    private String fullName;

    private boolean isSelected;

    public BookEditAuthorDto(AuthorDto author) {
        this.id = author.getId();
        this.fullName = author.getFullName();
        this.isSelected = false;
    }

}
