package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    BookService bookService;


    @DisplayName("findById должен загружать список авторов")
    @Test
    void findById_shouldFetchRelatedAuthors() {
        long bookId = 1L;

        Book actualBook = bookService.findById(bookId)
                .map(BookDto::toDomainObject)
                .orElseThrow();

        assertThatNoException().isThrownBy(
                () -> actualBook.getAuthors().size()
        );

    }

    @DisplayName("findById должен загружать жанр")
    @Test
    void findById_shouldFetchRelatedGenre() {
        long bookId = 1L;

        Book actualBook = bookService.findById(bookId)
                .map(BookDto::toDomainObject)
                .orElseThrow();

        assertThatNoException().isThrownBy(
                () -> actualBook.getGenre().getName()
        );

    }
}