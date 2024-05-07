package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;


    @Override
    public Optional<BookCommentDto> findById(long id) {
        BookComment bookComment = bookCommentRepository.findById(id).orElse(null);

        if(bookComment == null){
            return Optional.empty();
        }

        return Optional.of(
                new BookCommentDto(bookComment)
        );
    }

    @Override
    public List<BookCommentDto> findAllByBookId(long bookId) {

        List<BookComment> bookComments = bookCommentRepository.findAllByBookId(bookId);
        return bookComments.stream()
                .map(BookCommentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookCommentDto insert(long bookId, String text) {
        BookComment comment = bookCommentRepository.save(
                new BookComment(0, bookId, text)
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
    @Transactional
    public void deleteById(long id) {
        bookCommentRepository.deleteById(id);
    }
}
