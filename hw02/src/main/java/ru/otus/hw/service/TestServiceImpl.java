package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionService questionService;

    @Override
    public TestResult executeTestFor(Student student) {

        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();

        var testResult = new TestResult(student);

        for (Question question : questions) {
            questionService.print(question);
        }

        return testResult;
    }


//    @Override
//    public TestResult executeTestFor(Student student) {
//        ioService.printLine("");
//        ioService.printFormattedLine("Please answer the questions below%n");
//        var questions = questionDao.findAll();
//        var testResult = new TestResult(student);
//
//        for (var question: questions) {
//            var isAnswerValid = false; // Задать вопрос, получить ответ
//            testResult.applyAnswer(question, isAnswerValid);
//        }
//        return testResult;
//    }
}
