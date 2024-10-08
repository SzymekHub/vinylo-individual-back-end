package s3.individual.vinylo.controllers.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {
    
    public CustomNotFoundException(String message) {
        super(message);  // Only the specific message should be passed here.
    }
}
