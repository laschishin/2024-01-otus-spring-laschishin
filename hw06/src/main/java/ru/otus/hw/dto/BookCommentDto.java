package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.BookComment;

@Data
@AllArgsConstructor
public class BookCommentDto {

    private long id;

    private long bookId;

    private String textContent;

    public BookComment toDomainObject() {
        return new BookComment(id, bookId, textContent);
    }

    public static BookCommentDto toDto(BookComment comment) {
        return new BookCommentDto(
                comment.getId(),
                comment.getBookId(),
                comment.getTextContent()
        );
    }

}
