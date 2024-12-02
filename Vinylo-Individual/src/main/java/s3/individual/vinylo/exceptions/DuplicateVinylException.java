package s3.individual.vinylo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateVinylException extends RuntimeException {

    public DuplicateVinylException(String message) {

        super(message);
    }
}
