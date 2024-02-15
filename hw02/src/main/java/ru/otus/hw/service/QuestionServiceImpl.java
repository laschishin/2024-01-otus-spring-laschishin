package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final IOService ioService;

    @Override
    public void print(Question question) {

        ioService.printLine(question.text());

        for (Answer answer : question.answers()) {
            ioService.printLine("* " + answer.text());
        }

        ioService.printFormattedLine("%n");

    }
}
