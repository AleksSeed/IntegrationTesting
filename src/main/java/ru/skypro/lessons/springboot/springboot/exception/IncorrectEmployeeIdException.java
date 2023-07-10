package ru.skypro.lessons.springboot.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IncorrectEmployeeIdException extends Throwable {
    @ExceptionHandler
    public ResponseEntity<?> handlerSQLException(IndexOutOfBoundsException indexOutOfBoundsException) {
        return new ResponseEntity<>("Индекс вне диапазона", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerNumberFormatException(NumberFormatException numberFormatException) {
        return new ResponseEntity<>("Формат номера неверен!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerReportNotFoundException(ReportNotFoundException reportNotFoundException) {
        return new ResponseEntity<>("Отчет не найден", HttpStatus.NOT_FOUND);
    }

}
