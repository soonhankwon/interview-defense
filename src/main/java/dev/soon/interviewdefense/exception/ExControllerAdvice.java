package dev.soon.interviewdefense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult handleIllegalArgEx(IllegalArgumentException ex) {
        return new ErrorResult(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ErrorResult handleApiEx(ApiException ex) {
        CustomErrorCode customErrorCode = ex.getCustomErrorCode();
        return new ErrorResult(customErrorCode.getHttpStatus(), customErrorCode.getMessage());
    }
}
