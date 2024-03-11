package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final StreamsIOService ioService;

    @Override
    public void print(Question question) {

        ioService.printFormattedLine("%n%s", question.text());

        int answerNumber = 1;
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("%s. %s ", answerNumber, answer.text());
            answerNumber++;
        }

    }

    @Override
    public boolean isAnswerCorrect(Question question, int answerNumber) {
        int answerIndex = answerNumber - 1;
        return question.answers().get(answerIndex).isCorrect();
    }

    @Override
    public int getAnswersCount(Question question) {
        return question.answers().size();
    }
}
