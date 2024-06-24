package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
public class BookCommentConverter {

    public String bookCommentToString(BookComment bookComment) {
        return "Id: %d, Content: %s".formatted(bookComment.getId(), bookComment.getTextContent());
    }

}
