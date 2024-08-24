package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {

        Book book = bookRepository.findById(id)
                .orElse(null);

        if (book == null) {
            return Optional.empty();
        }
        return Optional.of(
                new BookDto(book)
        );

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {

        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

    }

}
