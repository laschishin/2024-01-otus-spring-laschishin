package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
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

        if(book == null) {
            return Optional.empty();
        }
        return Optional.of(
                new BookDto(book)
        );

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {

        List<Book> books =  bookRepository.findAll();

        return books.stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public BookDto insert(String title, Set<Long> authorsIds, long genresId) {

        Book book = save(0, title, authorsIds, genresId);

        return new BookDto(book);

    }

    @Override
    @Transactional
    public BookDto update(long id, String title, Set<Long> authorsIds, long genresId) {

        Book book = save(id, title, authorsIds, genresId);

        return new BookDto(book);

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, Set<Long> authorsIds, long genreId) {

        if (isEmpty(authorsIds)) {
            throw new IllegalArgumentException("Authors ids must not be null");
        }

        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var authors = authorRepository.findAllByIds(authorsIds);
        if (isEmpty(authorsIds) || authorsIds.size() != authors.size()) {
            throw new EntityNotFoundException("One or all authors with ids %s not found".formatted(authorsIds));
        }

        var book = new Book(id, title, authors, genre);
        return bookRepository.save(book);

    }

}
