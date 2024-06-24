package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByIdIn(Set<Long> ids);

}
