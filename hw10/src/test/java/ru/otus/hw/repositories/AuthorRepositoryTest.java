package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring Data для работы с авторами")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private static final Set<Long> AUTHORS_LIST = Set.of(1L, 2L);

    private List<Author> dbAuthors;


    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {

        List<Author> expectedAuthors = em.getEntityManager()
                .createQuery("select a from Author a")
                .getResultList();
        List<Author> actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен загружать список авторов по списку id")
    @Test
    void shouldReturnCorrectAuthorsById() {

        List<Author> expectedAuthors = em.getEntityManager()
                .createQuery("select a from Author a where a.id in (:ids)")
                .setParameter("ids", AUTHORS_LIST)
                .getResultList();

        List<Author> actualAuthorsList = authorRepository.findAllByIdIn(AUTHORS_LIST);

        assertThat(actualAuthorsList).containsExactlyElementsOf(expectedAuthors);
    }

}
