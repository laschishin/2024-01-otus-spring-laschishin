package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<AuthorDto> findAll();
    List<AuthorDto> findAllByIdIn(Set<Long> ids);

    Optional<AuthorDto> findById(long authorId);

}
