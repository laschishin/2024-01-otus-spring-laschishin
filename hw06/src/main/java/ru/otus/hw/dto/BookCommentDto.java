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

    public BookCommentDto(BookComment comment) {
        this.id = comment.getId();
        this.bookId = comment.getBookId();
        this.textContent = comment.getTextContent();
    }

}
