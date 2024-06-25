package ru.otus.hw.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEditAuthorDto {

    private AuthorDto author;

    private String fullNameDashed;

    private Boolean isSelected;

    public BookEditAuthorDto(AuthorDto author) {
        this.author = author;
    }

//    public static List<BookEditAuthorDto> toDto(List<Author> authors) {
//        return authors.stream()
//                .map(BookEditAuthorDto::new)
//                .collect(Collectors.toList());
//    }

}
