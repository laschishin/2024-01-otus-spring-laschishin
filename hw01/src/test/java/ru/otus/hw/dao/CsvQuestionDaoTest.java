package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.UtilsService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider testFileNameProvider;
    @Mock
    private UtilsService utilsService;

    @Test
    void findAllFullTest() {

        String testFileName = "questions.csv";
        String mockedCSVContent = """
                First line should be skipped
                Is there life on Mars?;Science doesn't know this yet%true|Certainly. The red UFO is from Mars. And green is from Venus%false|Absolutely not%false
                """;
        InputStream questionsInputStream = new ByteArrayInputStream(mockedCSVContent.getBytes());

        List<Question> expectedQuestions = List.of(
                new Question("Is there life on Mars?",
                        List.of(
                                new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                                new Answer("Absolutely not", false)
                        ))
        );

        Mockito.when(testFileNameProvider.getTestFileName())
                .thenReturn(testFileName);

        Mockito.when(utilsService.getFileAsStream(testFileName))
                .thenReturn(questionsInputStream);

        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(testFileNameProvider, utilsService);

        List<Question> actualQuestions = csvQuestionDao.findAll();

        Assertions.assertEquals(expectedQuestions, actualQuestions);

        Mockito.verify(testFileNameProvider, Mockito.times(1)).getTestFileName();

        Mockito.verify(utilsService, Mockito.times(1)).getFileAsStream(testFileName);

        Mockito.verifyNoMoreInteractions(testFileNameProvider, utilsService);

    }

}