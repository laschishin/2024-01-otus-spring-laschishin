package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    JdbcAuthorRepository jdbcAuthorRepository;

    private List<Author> dbAuthors;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {

        List<Author> expectedAuthors = dbAuthors;
        List<Author> actualAuthors = jdbcAuthorRepository.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен загружать список авторов по списку id")
    @Test
    void shouldReturnCorrectAuthorsById() {

        List<Author> expectedAuthors = dbAuthors;
        Set<Long> expectedAuthorsIdList = expectedAuthors.stream()
                .map(Author::getId).collect(Collectors.toSet());

        List<Author> actualAuthorsList = jdbcAuthorRepository.findAllByIds(expectedAuthorsIdList);

        assertThat(actualAuthorsList).isEqualTo(expectedAuthors);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }


}