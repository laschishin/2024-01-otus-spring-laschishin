package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        String testFileName = fileNameProvider.getTestFileName();

        InputStream is = getFileAsStream(testFileName);

        List<QuestionDto> questionDtoList;
        try (Reader r = new InputStreamReader(is)) {
            questionDtoList = new CsvToBeanBuilder(r)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();

        } catch (IOException e) {
            throw new QuestionReadException("File reading error: " + testFileName, e);
        }

        return questionDtoList.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

    }

    private InputStream getFileAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName); // TODO развернуть и посмотреть что будет с несуществующим файлом

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return inputStream;

    }
}
