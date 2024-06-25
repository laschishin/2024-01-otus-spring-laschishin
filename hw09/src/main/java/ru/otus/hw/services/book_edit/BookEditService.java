package ru.otus.hw.services.book_edit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditAuthorDto;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BookEditService {

    public List<BookEditAuthorDto> prepareAuthorsList(BookDto book,
                                                      List<AuthorDto> authors) {

        List<Long> authorIdList = book.getAuthors().stream()
                .map(AuthorDto::getId)
                .toList();

        List<BookEditAuthorDto> bookEditAuthorDto = authors.stream()
                .map(BookEditAuthorDto::new)
                .toList();

        bookEditAuthorDto.forEach(authorDto -> authorDto.setIsSelected(
                authorIdList.contains(authorDto.getAuthor().getId())
        ));

        bookEditAuthorDto.forEach(authorDto -> authorDto.setFullNameDashed(
                generateUnderscoreFullName(authorDto.getAuthor().getFullName())
        ));

        return bookEditAuthorDto;
    }

    public String generateUnderscoreFullName(String authorFullName) {
        return authorFullName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "-");
    }

}
