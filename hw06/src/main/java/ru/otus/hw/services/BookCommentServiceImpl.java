package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;

    private final BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<BookCommentDto> findById(long id) {

        BookComment bookComment = bookCommentRepository.findById(id)
                .orElse(null);

        if(bookComment == null){
            return Optional.empty();
        }

        return Optional.of(
                new BookCommentDto(bookComment)
        );

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCommentDto> findAllByBookId(long bookId) {

        List<BookComment> bookComments = bookCommentRepository.findAllByBookId(bookId);

        return bookComments.stream()
                .map(BookCommentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookCommentDto insert(long bookId, String text) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)) );

        BookComment comment = bookCommentRepository.save(
                new BookComment(0, book, text)
        );

        return new BookCommentDto(comment);

    }

    @Override
    @Transactional
    public BookCommentDto update(long id, String text) {

        BookComment comment = bookCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));

        comment.setTextContent(text);

        return new BookCommentDto(
                bookCommentRepository.save(comment)
        );

    }

    @Override
    public void deleteById(long id) {
        bookCommentRepository.deleteById(id);
    }
}
