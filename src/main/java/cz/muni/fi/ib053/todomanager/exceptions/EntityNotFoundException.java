package cz.muni.fi.ib053.todomanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used for reporting that entity with given type and id does not exists.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The requested entity was not found")
public class EntityNotFoundException extends RuntimeException {

        public EntityNotFoundException(String entityType, Long id) {
                super(String.format("%s with id=%d can not be found!", entityType, id));
        }

}
