package ru.otus.hw.services;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.UpdateBookRequest;

@Service
public class BookMapperService {

    public BookDto map(@NonNull BookDto dto,
                       @NonNull UpdateBookRequest request) {

        if (request.getTitle() != null) {
            dto.setTitle(request.getTitle());
        }

        return dto;
    }

}
