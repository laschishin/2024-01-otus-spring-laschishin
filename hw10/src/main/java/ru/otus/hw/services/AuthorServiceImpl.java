package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream()
                .map(AuthorDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorDto> findById(long authorId) {

        Author author = authorRepository.findById(authorId)
                .orElse(null);

        if(author == null) {
            return Optional.empty();
        }

        return Optional.of(
                new AuthorDto(author)
        );
    }

}
