package ru.otus.hw.service;

import java.io.InputStream;

public class UtilsServiceImpl implements UtilsService {

    @Override
    public InputStream getFileAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return inputStream;
    }
}
