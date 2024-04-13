package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JpqlAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;
    
    
    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("""
                        select a
                          from Author a
                        """,
                Author.class
        );
        return query.getResultList();
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        TypedQuery<Author> query = em.createQuery("""
                        select a
                          from Author a
                         where a.id in (:ids)
                        """,
                Author.class
        );
        query.setParameter("ids", ids);
        return query.getResultList();
    }

}
