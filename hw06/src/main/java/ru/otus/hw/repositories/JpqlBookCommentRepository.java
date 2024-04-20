package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlBookCommentRepository implements BookCommentRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<BookComment> findById(long id) {

        return Optional.ofNullable(em.find(BookComment.class, id));
    }

    @Override
    public List<BookComment> findAllByBookId(long bookId) {
        TypedQuery<BookComment> query = em.createQuery("""
                        select bc
                          from BookComment bc
                         where bc.bookId = :bookId
                        """,
                BookComment.class
        );
        query.setParameter("bookId", bookId);
        return query.getResultList();

    }

}
