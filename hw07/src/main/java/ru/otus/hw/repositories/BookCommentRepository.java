package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends CrudRepository<BookComment, Long> {
    Optional<BookComment> findById(long id);

    List<BookComment> findAllByBookId(long bookId);

    BookComment save(BookComment comment);

    void deleteById(long id);

}
