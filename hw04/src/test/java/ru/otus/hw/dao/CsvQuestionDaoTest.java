package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.UtilsService;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CsvQuestionDao.class})
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockBean
    private AppProperties props;

    @MockBean
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

        String testFileName = "unit_test_questions.csv";
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream testInputStream = classLoader.getResourceAsStream(testFileName);

        when(props.getTestFileName()).thenReturn(testFileName);
        when(utilsService.getFileAsStream(testFileName)).thenReturn(testInputStream);

        List<Question> actualQuestions = csvQuestionDao.findAll();

        assertEquals(expectedQuestions, actualQuestions);

        verify(props).getTestFileName();
        verify(utilsService).getFileAsStream(testFileName);
        verifyNoMoreInteractions(props, utilsService);

    }

}