package dev.soon.interviewdefense.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleIllegalArgEx(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        log.error("error message={}", ex.getMessage());
        response.sendError(404);
    }

    @ExceptionHandler({RuntimeException.class})
    public void handleRuntimeEx(RuntimeException ex, HttpServletResponse response) throws IOException {
        log.error("error message={}", ex.getMessage());
        response.sendError(400);
    }
}
