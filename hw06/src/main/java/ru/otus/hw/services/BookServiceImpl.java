package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, Set<Long> authorsIds, long genresId) {
        return save(0, title, authorsIds, genresId);
    }

    @Override
    public Book update(long id, String title, Set<Long> authorsIds, long genresId) {
        return save(id, title, authorsIds, genresId);
    }

    @Override
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
