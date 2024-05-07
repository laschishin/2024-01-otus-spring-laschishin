package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;


    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {

        List<Genre> genreList = genreRepository.findAll();

        return genreList.stream()
                .map(GenreDto::new)
                .collect(Collectors.toList());
    }

}
