package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public List<Comment> findAllByBookId(long bookId) {
        TypedQuery<Comment> query = em.createQuery("""
                        select c
                          from Comment c
                         where c.bookId = :bookId
                        """,
                Comment.class
        );
        query.setParameter("bookId", bookId);
        return query.getResultList();

    }

    @Override
    public Optional<Comment> findById(long id) {

        return Optional.ofNullable(em.find(Comment.class, id));

    }

}
