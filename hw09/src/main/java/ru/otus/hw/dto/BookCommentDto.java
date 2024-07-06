package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;

@Data
@AllArgsConstructor
public class BookCommentDto {

    private long id;

    private Book book;

    private String textContent;

    public BookCommentDto(BookComment comment) {
        this.id = comment.getId();
        this.book = comment.getBook();
        this.textContent = comment.getTextContent();
    }

    public BookComment toDomainObject() {
        return new BookComment(id, book, textContent);
    }

}
