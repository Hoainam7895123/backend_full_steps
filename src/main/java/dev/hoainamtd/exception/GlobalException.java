package dev.hoainamtd.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class }) // xử lý lỗi của class này
    @ResponseStatus(HttpStatus.BAD_REQUEST) // mã lỗi trả về
    public ErrorResponse handleValidationException(Exception e, WebRequest webRequest) {
        System.out.println("============= handleValidationException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=", ""));

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            errorResponse.setError("Payload Invalid");
                } else if (e instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse.setError("PathVariable Invalid");
        }

        errorResponse.setMessage(message);

        return errorResponse;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class }) // xử lý lỗi của class này
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // mã lỗi trả về
    public ErrorResponse handleInternalServerError(Exception e, WebRequest webRequest) {
        System.out.println("============= handleInternalServerError");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=", ""));

        if (e instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setError("Payload Invalid");
            errorResponse.setMessage("Failed to convert value of type");
        }


        return errorResponse;
    }
}
