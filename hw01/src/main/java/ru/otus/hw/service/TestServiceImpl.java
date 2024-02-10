package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;
    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {

        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = csvQuestionDao.findAll();

        for (Question question : questions) {
            printQuestion(question);
        }

    }

    private void printQuestion(Question question) {

        ioService.printLine(question.text());

        for (Answer answer : question.answers()) {
            ioService.printLine("* " + answer.text());
        }

        ioService.printFormattedLine("%n");

    }
}
