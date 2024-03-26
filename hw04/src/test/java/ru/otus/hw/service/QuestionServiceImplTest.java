package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@MockBean(classes = {
        StreamsIOService.class
})
public class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

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