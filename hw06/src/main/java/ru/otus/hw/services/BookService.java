package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<BookDto> findById(long id);

    List<Book> findAll();

    Book insert(String title, Set<Long> authorsIds, long genresId);

    Book update(long id, String title, Set<Long> authorsIds, long genresId);

    void deleteById(long id);

}
