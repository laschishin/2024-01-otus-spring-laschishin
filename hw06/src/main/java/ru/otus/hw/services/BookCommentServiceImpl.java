package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookCommentDto;
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
                BookCommentDto.toDto(bookComment)
        );
    }

    @Override
    public List<BookCommentDto> findAllByBookId(long bookId) {

        List<BookComment> bookComments = bookCommentRepository.findAllByBookId(bookId);
        return bookComments.stream()
                .map(BookCommentDto::toDto)
                .collect(Collectors.toList());
    }
}
