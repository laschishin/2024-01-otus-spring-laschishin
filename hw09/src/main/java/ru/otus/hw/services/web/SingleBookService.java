package ru.otus.hw.services.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.*;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SingleBookService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookRepository bookRepository;


    public Map<String, Object> createBookGetAttributes() {

        Map<String, Object> viewEntities = new HashMap<>();

        viewEntities.put("book", new BookDto());

        List<AuthorDto> authorsFullList = authorService.findAll();
        List<BookEditAuthorDto> authorsForViewList = authorsFullList.stream()
                .map(BookEditAuthorDto::new)
                .toList();
        viewEntities.put("authors_list", authorsForViewList);


        List<GenreDto> genresFullList = genreService.findAll();
        List<BookEditGenreDto> genresForViewList = genresFullList.stream()
                .map(BookEditGenreDto::new)
                .toList();
        viewEntities.put("genres_list", genresForViewList);

        return viewEntities;

    }


    public Map<String, Object> editBookGetAttributes(long bookId) {

        Map<String, Object> viewEntities = new HashMap<>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        viewEntities.put("book", book);

        List<AuthorDto> authorsFullList = authorService.findAll();
        List<BookEditAuthorDto> authorsForViewList = prepareAuthorsList(book, authorsFullList);
        viewEntities.put("authors_list", authorsForViewList);

        List<GenreDto> genresFullList = genreService.findAll();
        List<BookEditGenreDto> genresForViewList = prepareGenresList(book, genresFullList);
        viewEntities.put("genres_list", genresForViewList);

        return viewEntities;

    }

    public void processUpdateBook(Book book) {

        bookRepository.save(book);
    }

    public Map<String, Object> deleteBookGetAttributes(long bookId) {

        Map<String, Object> viewEntities = new HashMap<>();

        BookDto book = bookService.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        viewEntities.put("book", book);

        viewEntities.put("authors_list", book.getAuthors());

        viewEntities.put("genre", book.getGenre());

        return viewEntities;

    }

    public void processDeleteBook(long bookId) {

        bookRepository.deleteById(bookId);
    }

    private List<BookEditAuthorDto> prepareAuthorsList(BookDto book,
                                                       List<AuthorDto> authorsList) {

        List<Long> authorIdsList = book.getAuthors().stream()
                .map(AuthorDto::getId)
                .toList();

        List<BookEditAuthorDto> bookEditAuthorDto = authorsList.stream()
                .map(BookEditAuthorDto::new)
                .toList();

        bookEditAuthorDto.forEach(authorDto -> authorDto.setSelected(
                authorIdsList.contains(authorDto.getId())
        ));

        return bookEditAuthorDto;
    }

    private List<BookEditGenreDto> prepareGenresList(BookDto book,
                                                     List<GenreDto> genresList) {
        List<BookEditGenreDto> bookEditGenreList = genresList.stream()
                .map(BookEditGenreDto::new)
                .toList();

        bookEditGenreList.forEach(genreDto -> genreDto.setSelected(
                genreDto.getId() == book.getGenre().getId()
        ));

        return bookEditGenreList;
    }

}
