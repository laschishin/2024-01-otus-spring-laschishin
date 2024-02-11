package ru.otus.hw.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    @Test
    void shouldHaveCorrectConstructor() {

        String questionText = "2 + 2 * 2?";

        Question question = new Question(questionText,
                List.of(new Answer("8", false),
                        new Answer("6", true)));

        assertEquals(questionText, question.text());

    }
}