package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.Application;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {Application.class})
@MockBean(classes = {
        TestRunnerServiceImpl.class,
        StudentService.class,
        ResultService.class
})
class TestServiceImplTest {

    @Autowired
    TestServiceImpl testService;

    @MockBean
    LocalizedIOService ioService;

    @MockBean
    QuestionDao questionDao;

    @MockBean
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

        doNothing().when(ioService).printLine("");
        doNothing().when(ioService).printLineLocalized("TestService.answer.the.questions");
        when(questionDao.findAll()).thenReturn(expectedQuestionsList);
        doNothing().when(questionService).print(expectedQuestion);
        when(questionService.getAnswersCount(expectedQuestion)).thenReturn(expectedMaxAnswerNumber);
        when(ioService.readIntForRangeWithPromptLocalized(1, expectedMaxAnswerNumber,
                "TestService.your.answer.is", "TestService.please.input.number"))
                .thenReturn(expectedUserAnswerNumber);
        when(questionService.isAnswerCorrect(expectedQuestion, expectedUserAnswerNumber)).thenReturn(true);

        TestResult actualTestResult = testService.executeTestFor(student);

        assertEquals(expectedTestResult, actualTestResult);

        verify(ioService, times(2)).printLine("");
        verify(ioService, times(1)).printLineLocalized("TestService.answer.the.questions");
        verify(questionDao).findAll();
        verify(questionService).print(expectedQuestion);
        verify(questionService).getAnswersCount(expectedQuestion);
        verify(ioService).readIntForRangeWithPromptLocalized(1, expectedMaxAnswerNumber,
                "TestService.your.answer.is", "TestService.please.input.number");
        verify(questionService).isAnswerCorrect(expectedQuestion, expectedUserAnswerNumber);
        verifyNoMoreInteractions(ioService, questionDao, questionService);

    }
}