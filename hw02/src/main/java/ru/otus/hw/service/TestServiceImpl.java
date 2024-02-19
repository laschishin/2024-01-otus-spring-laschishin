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

        ioService.printFormattedLine("Please answer the questions below");

        List<Question> questions = questionDao.findAll();

        var testResult = new TestResult(student);

        for (Question question : questions) {
            questionService.print(question);

            int maxAnswerNumber = questionService.getAnswersCount(question) + 1;
            int userAnswerNumber = ioService.readIntForRangeWithPrompt(1, maxAnswerNumber, "Your answer is: ",
                    String.format("Please input a number between 1 and %s", maxAnswerNumber));

            boolean isAnswerCorrect = questionService.isAnswerCorrect(question, userAnswerNumber);

            testResult.applyAnswer(question, isAnswerCorrect);
        }

        return testResult;
    }
}
