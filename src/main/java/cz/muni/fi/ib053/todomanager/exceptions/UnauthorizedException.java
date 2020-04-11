package cz.muni.fi.ib053.todomanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used for reporting that user with given username tried to login into application with wrong password.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

        public UnauthorizedException(String username) {
                super(String.format("Combination of your username and password is no correct. Used username was \"%s\"", username));
        }
}
