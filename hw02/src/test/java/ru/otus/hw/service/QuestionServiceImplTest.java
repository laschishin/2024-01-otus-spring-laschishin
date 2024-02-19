package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    private static QuestionServiceImpl questionService;

    @Mock
    private static IOService ioService;

    @BeforeAll
    static void setUp() {
        questionService = new QuestionServiceImpl(ioService);
    }

    @Test
    void isAnswerCorrect_when_TwoAnswersAndAnswerNumberIsTwo() {

        Question question = new Question("2 + 2 * 2",
                List.of(new Answer("8", false),
                        new Answer("6", true))
        );

        assertTrue(questionService.isAnswerCorrect(question, 2));

    }

    @Test
    void isAnswerCorrect_when_TwoAnswersAndAnswerNumberIsOne() {

        Question question = new Question("2 + 2 * 2",
                List.of(new Answer("8", false),
                        new Answer("6", true))
        );

        assertFalse(questionService.isAnswerCorrect(question, 1));

    }

    @Test
    void isAnswerCorrect_when_ZeroAnswers() {

        Question question = new Question("2 + 2 * 2", new ArrayList<>());

        assertThrows(IndexOutOfBoundsException.class, () -> questionService.isAnswerCorrect(question, 1));

    }

}