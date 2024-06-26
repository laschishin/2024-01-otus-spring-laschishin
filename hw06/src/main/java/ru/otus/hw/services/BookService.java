package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, Set<Long> authorsIds, long genresId);

    BookDto update(long id, String title, Set<Long> authorsIds, long genresId);

    void deleteById(long id);

}
