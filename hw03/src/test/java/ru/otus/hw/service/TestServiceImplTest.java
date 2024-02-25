package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    TestServiceImpl testService;

    @Mock
    LocalizedIOService ioService;

    @Mock
    QuestionDao questionDao;

    @Mock
    QuestionService questionService;

    @Test
    void executeTestFor() {

        Question expectedQuestion = new Question("Is there life on Mars?",
                List.of(
                        new Answer("Science doesn't know this yet", true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                        new Answer("Absolutely not", false)
                ));
        List<Question> expectedQuestionsList = List.of(expectedQuestion);

        int expectedMaxAnswerNumber = 3;
        int expectedUserAnswerNumber = 1;

        Student student = new Student("Ivan", "Ivanov");
        TestResult expectedTestResult = new TestResult(student);
        expectedTestResult.applyAnswer(expectedQuestion, true);

        doNothing().when(ioService).printFormattedLine("Please answer the questions below");
        when(questionDao.findAll()).thenReturn(expectedQuestionsList);
        doNothing().when(questionService).print(expectedQuestion);
        when(questionService.getAnswersCount(expectedQuestion)).thenReturn(expectedMaxAnswerNumber);
        when(ioService.readIntForRangeWithPrompt(1, expectedMaxAnswerNumber, "Your answer is: ",
                String.format("Please input a number between 1 and %s", expectedMaxAnswerNumber)))
                .thenReturn(expectedUserAnswerNumber);
        when(questionService.isAnswerCorrect(expectedQuestion, expectedUserAnswerNumber)).thenReturn(true);

        TestResult actualTestResult = testService.executeTestFor(student);

        assertEquals(expectedTestResult, actualTestResult);

        verifyNoMoreInteractions(ioService, questionDao, questionService);

    }
}