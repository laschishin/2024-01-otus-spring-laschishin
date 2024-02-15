package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.UtilsService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final UtilsService utilsService;

    @Override
    public List<Question> findAll() {

        String testFileName = fileNameProvider.getTestFileName();

        InputStream is = utilsService.getFileAsStream(testFileName);

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
        } catch (RuntimeException e) {
            throw new QuestionReadException("File parsing error: " + testFileName, e);
        }

        return questionDtoList.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

    }

}
