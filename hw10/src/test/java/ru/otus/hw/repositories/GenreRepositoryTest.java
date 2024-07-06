package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    private static final long FIRST_GENRE_ID = 1;


    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {

        List<Genre> expectedGenres = em.getEntityManager()
                .createQuery("select g from Genre g")
                .getResultList();

        List<Genre> actualGenres = genreRepository.findAll();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {

        Genre expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        Genre actualGenre = genreRepository.getReferenceById(FIRST_GENRE_ID);

        assertThat(actualGenre)
                .isEqualTo(expectedGenre);
    }

}
