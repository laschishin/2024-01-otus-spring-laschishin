package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAll();

    List<Author> findAllByIdIn(Set<Long> ids);

}
