package ru.otus.hw.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class UtilsServiceImpl implements UtilsService {

    @Override
    public InputStream getFileAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new QuestionReadException("File not found: " + fileName);
        }

        return inputStream;
    }
}
