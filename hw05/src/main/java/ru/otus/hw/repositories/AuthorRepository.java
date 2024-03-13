package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Set;

public interface AuthorRepository {
    List<Author> findAll();

    List<Author> findAllByIds(Set<Long> ids);

}
