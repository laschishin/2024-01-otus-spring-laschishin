package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.UtilsService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @Mock
    private UtilsService utilsService;

    @Test
    void findAllFullTest() {

        List<Question> expectedQuestions = List.of(
                new Question("Is there life on Mars?",
                        List.of(
                                new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                                new Answer("Absolutely not", false)
                        ))
        );

        String testFileName = "questions.csv";
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream testInputStream = classLoader.getResourceAsStream(testFileName);

        when(testFileNameProvider.getTestFileName())
                .thenReturn(testFileName);

        when(utilsService.getFileAsStream(testFileName))
                .thenReturn(testInputStream);

        List<Question> actualQuestions = csvQuestionDao.findAll();

        assertEquals(expectedQuestions, actualQuestions);

        verify(testFileNameProvider).getTestFileName();

        verify(utilsService).getFileAsStream(testFileName);

        verifyNoMoreInteractions(testFileNameProvider, utilsService);

    }

}