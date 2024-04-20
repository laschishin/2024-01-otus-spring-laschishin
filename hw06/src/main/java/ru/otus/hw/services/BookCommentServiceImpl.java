package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;


    @Override
    public Optional<BookComment> findById(long id) {
        return bookCommentRepository.findById(id);
    }

    @Override
    public List<BookComment> findAllByBookId(long bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }
}
