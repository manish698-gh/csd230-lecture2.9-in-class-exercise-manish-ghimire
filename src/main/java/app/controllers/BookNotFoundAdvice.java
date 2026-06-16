package app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice tells Spring that this class is used to
 * render JSON specifically for error scenarios across ALL controllers.
 */
@RestControllerAdvice
class BookNotFoundAdvice {

    /**
     * @ExceptionHandler triggers this method ONLY when a
     * BookNotFoundException occurs.
     * @ResponseStatus(HttpStatus.NOT_FOUND) ensures the browser/Postman
     * receives a true 404 status code in the header.
     */
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFoundHandler(BookNotFoundException ex) {
        // We return the raw message string.
        // Spring converts this into a JSON-compatible body.
        return ex.getMessage();
    }
}