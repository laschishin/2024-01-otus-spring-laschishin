//package ru.otus.hw.commands;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.hw.converters.AuthorConverter;
//import ru.otus.hw.dto.AuthorDto;
//import ru.otus.hw.services.AuthorService;
//
//import java.util.stream.Collectors;
//
//@ShellComponent
//@RequiredArgsConstructor
//public class AuthorCommands {
//
//    private final AuthorService authorService;
//
//    private final AuthorConverter authorConverter;
//
//
//    @ShellMethod(value = "Find all authors", key = "aa")
//    public String findAllAuthors() {
//        return authorService.findAll().stream()
//                .map(AuthorDto::toDomainObject)
//                .map(authorConverter::authorToString)
//                .collect(Collectors.joining("," + System.lineSeparator()));
//    }
//
//}