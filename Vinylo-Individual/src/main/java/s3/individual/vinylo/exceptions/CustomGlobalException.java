package s3.individual.vinylo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomGlobalException extends RuntimeException {

    public CustomGlobalException(String message) {
        super(message);
    }
}