package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        String testFileName = fileNameProvider.getTestFileName();

        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings


        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

        InputStream is = getFileAsStream(testFileName);

        List<QuestionDto> beans;
        try (Reader r = new InputStreamReader(is)) {
            beans = new CsvToBeanBuilder(r)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Question> questions = beans.stream()
                .map(bean -> bean.toDomainObject())
                .collect(Collectors.toList());

        return questions;
    }

    private InputStream getFileAsStream(String fileName){

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName); // TODO развернуть и посмотреть что будет с несуществующим файлом

        if(inputStream == null){
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return inputStream;

    }
}
