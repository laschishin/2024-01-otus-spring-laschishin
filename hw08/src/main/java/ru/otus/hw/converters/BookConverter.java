//package ru.otus.hw.converters;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import ru.otus.hw.models.Book;
//
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class BookConverter {
//
//    private final AuthorConverter authorConverter;
//
//    private final GenreConverter genreConverter;
//
//
//    public String bookToString(Book book) {
//        var authorsString = book.getAuthors().stream()
//                .map(authorConverter::authorToString)
//                .map("{%s}"::formatted)
//                .collect(Collectors.joining(", "));
//        return "Id: %d, title: %s, genre: {%s}, authors: [%s]".formatted(
//                book.getId(),
//                book.getTitle(),
//                genreConverter.genreToString(book.getGenre()),
//                authorsString);
//    }
//
//}
