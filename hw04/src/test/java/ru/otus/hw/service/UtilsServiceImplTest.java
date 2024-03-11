package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsServiceImplTest {

    private UtilsServiceImpl utilsService;

    @Test
    void getFileAsStream_When_FileNotExists() {

        String nonExistingFileName = "///<>blabla.csv";
        utilsService = new UtilsServiceImpl();

        assertThrows(QuestionReadException.class, () -> utilsService.getFileAsStream(nonExistingFileName));

    }

}