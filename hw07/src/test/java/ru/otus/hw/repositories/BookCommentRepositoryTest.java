package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring Data для работы с комментариями к книгам ")
@DataJpaTest
class BookCommentRepositoryTest {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Autowired
    private TestEntityManager em;

    private static final long FIRST_BOOK_COMMENT_ID = 1;
    private static final long FIRST_BOOK_ID = 1;


    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectBookCommentById() {

        BookComment expectedComment = em.find(BookComment.class, FIRST_BOOK_COMMENT_ID);
        Optional<BookComment> actualComment = bookCommentRepository.findById(expectedComment.getId());

        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать все комментарии одной книги ")
    @Test
    void shouldReturnCorrectCommentsByBookId() {

        List<BookComment> expectedBookComments = em.getEntityManager()
                .createQuery("""
                        select bc
                          from BookComment bc
                         where bc.book.id = :book_id
                        """)
                .setParameter("book_id", FIRST_BOOK_ID)
                .getResultList();

        List<BookComment> actualBookComments = bookCommentRepository.findAllByBookId(FIRST_BOOK_ID);

        assertThat(actualBookComments).containsExactlyElementsOf(expectedBookComments);

    }


}