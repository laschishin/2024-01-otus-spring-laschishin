package ru.otus.hw.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEditAuthorDto {

    private long id;

    private String fullName;

    private boolean isSelected;

    public BookEditAuthorDto(AuthorDto author) {
        this.id = author.getId();
        this.fullName = author.getFullName();
        this.isSelected = false;
    }

}
