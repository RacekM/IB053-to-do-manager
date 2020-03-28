package cz.muni.fi.ib053.todomanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

        public UnauthorizedException(String username) {
                super(String.format("Combination of your username and password is no correct. Used username was \"%s\"", username));
        }
}
