package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.BookComment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlBookCommentRepository implements BookCommentRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<BookComment> findById(long id) {

        Map<String, Object> queryHints = new HashMap<>();
        queryHints.put("org.hibernate.readOnly", true);

        return Optional.ofNullable(em.find(BookComment.class, id, queryHints));
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

    @Override
    public BookComment save(BookComment comment) {
        if(comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        BookComment bc = new BookComment(
                comment.getId(),
                comment.getBookId(),
                comment.getTextContent()
        );
        return em.merge(bc);
    }

    @Override
    public void deleteById(long id) {

        BookComment comment = em.find(BookComment.class, id);

        if (comment == null) {
            throw new IllegalArgumentException("No comment found for id %d".formatted(id));
        }

        em.remove(comment);

    }

}
