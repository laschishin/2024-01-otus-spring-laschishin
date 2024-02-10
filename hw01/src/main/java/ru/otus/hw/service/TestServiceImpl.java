package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    private final PrintQuestionService printQuestionService;

    @Override
    public void executeTest() {

        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = csvQuestionDao.findAll();

        for (Question question : questions) {
            printQuestionService.print(question);
        }

    }

}
