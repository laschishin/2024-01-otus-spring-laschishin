package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionService {

    void print(Question question);

    boolean isAnswerCorrect(Question question, int answerNumber);

    int getAnswersCount(Question question);

}
