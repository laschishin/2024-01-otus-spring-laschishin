//package ru.otus.hw.services;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.otus.hw.dto.AuthorDto;
//import ru.otus.hw.models.Author;
//import ru.otus.hw.repositories.AuthorRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AuthorServiceImpl implements AuthorService {
//
//    private final AuthorRepository authorRepository;
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<AuthorDto> findAll() {
//        List<Author> authors = authorRepository.findAll();
//
//        return authors.stream()
//                .map(AuthorDto::new)
//                .collect(Collectors.toList());
//    }
//
//}
