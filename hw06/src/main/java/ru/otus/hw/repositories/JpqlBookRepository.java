package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<Book> findById(long id) {

        Book book = em.find(Book.class, id);

        return Optional.ofNullable(book);

//        TypedQuery<Book> query = em.createQuery("""
//                        select b
//                          from Book b
//                          join b.genre
//                          left join b.authors
//                         where b.id = :id
//                        """,
//                Book.class
//        );
//        query.setParameter("id", id);

//        Book resultBook;
//        try {
//            resultBook = query.getSingleResult();
//        }
//        catch (jakarta.persistence.NoResultException e) {
//            return Optional.empty();
//        }
//        return Optional.of(resultBook);
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("""
                        select b
                          from Book b
                          join fetch b.genre
                          left join fetch b.authors
                        """,
                Book.class
        );
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {

        Book book = em.find(Book.class, id);

        if (book == null) {
            throw new EntityNotFoundException("No book found for id %d".formatted(id));
        }

        em.remove(book);
    }

}
