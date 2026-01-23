package com.electronic.store.exception;

import com.electronic.store.helper.ApiCustomResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundExc.class)
    public ResponseEntity<ApiCustomResponse>ResourceNotFoundExc(ResourceNotFoundExc Ex){
        ApiCustomResponse build = ApiCustomResponse.builder()
                .message(Ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        logger.info("In the global handler");
        return new ResponseEntity<>(build,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> MethodArgumentNotValidException(MethodArgumentNotValidException Ex){
        List<ObjectError> allErrors = Ex.getBindingResult().getAllErrors();
        Map<String,Object>response=new HashMap<>();
        allErrors.forEach(error -> {
            String defaultMessage = error.getDefaultMessage();
            String objectName = ((FieldError)error).getField();
           response.put(objectName,defaultMessage);
        });
return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiCustomResponse>ResourceNotFoundExc(BadRequestException Ex){
        ApiCustomResponse build = ApiCustomResponse.builder()
                .message(Ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        logger.info("Image type is not valid");
        return new ResponseEntity<>(build,HttpStatus.BAD_REQUEST);
    }

}
