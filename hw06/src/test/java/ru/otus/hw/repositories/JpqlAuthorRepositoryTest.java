package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpql для работы с авторами")
@DataJpaTest
@Import(JpqlAuthorRepository.class)
class JpqlAuthorRepositoryTest {

    @Autowired
    private JpqlAuthorRepository jpqlAuthorRepository;

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
        List<Author> actualAuthors = jpqlAuthorRepository.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен загружать список авторов по списку id")
    @Test
    void shouldReturnCorrectAuthorsById() {

        List<Author> expectedAuthors = em.getEntityManager()
                .createQuery("select a from Author a where a.id in (:ids)")
                .setParameter("ids", AUTHORS_LIST)
                .getResultList();

        List<Author> actualAuthorsList = jpqlAuthorRepository.findAllByIds(AUTHORS_LIST);

        assertThat(actualAuthorsList).containsExactlyElementsOf(expectedAuthors);
    }

}
