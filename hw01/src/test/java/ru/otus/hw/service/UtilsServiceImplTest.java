package ru.otus.hw.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsServiceImplTest {

    private UtilsServiceImpl utilsService;

    @Test
    void getFileAsStream_When_FileNotExists() {

        String nonExistingFileName = "///<>blabla.csv";
        utilsService = new UtilsServiceImpl();

        assertThrows(IllegalArgumentException.class, () -> utilsService.getFileAsStream(nonExistingFileName));

    }

}