package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
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

        questions.stream().forEach(question -> ioService.printFormattedLine(String.valueOf(question)));

//        ioService.printFormattedLine(String.valueOf(questions.get(0)));
    }
}
