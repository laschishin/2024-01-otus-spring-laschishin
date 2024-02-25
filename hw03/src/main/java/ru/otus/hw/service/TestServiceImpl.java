package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionService questionService;


    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (Question question : questions) {
            questionService.print(question);

            int maxAnswerNumber = questionService.getAnswersCount(question);
            int userAnswerNumber = ioService.readIntForRangeWithPromptLocalized(1, maxAnswerNumber,
                    "TestService.your.answer.is", "TestService.input.number.between");

            boolean isAnswerCorrect = questionService.isAnswerCorrect(question, userAnswerNumber);

            testResult.applyAnswer(question, isAnswerCorrect);
        }

        return testResult;
    }

}
