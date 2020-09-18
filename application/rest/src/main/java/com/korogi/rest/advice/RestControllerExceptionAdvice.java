package com.korogi.rest.advice;

import static com.korogi.dto.ErrorDTO.newErrorDTO;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.korogi.core.util.UUIDGenerator;
import com.korogi.dto.ErrorDTO;
import com.korogi.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestControllerExceptionAdvice {

    private final UUIDGenerator uuidGenerator;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorDTO errorDTO = newErrorDTO()
                .status(NOT_FOUND.getReasonPhrase())
                .code(NOT_FOUND.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, NOT_FOUND);
    }

    // TODO fix
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException exception) {
//        ErrorDTO errorDTO = newErrorDTO()
//                .status(UNAUTHORIZED.getReasonPhrase())
//                .code(UNAUTHORIZED.value())
//                .message("You are either not authenticated or not authorized to view this resource")
//                .build();
//
//        return new ResponseEntity<>(errorDTO, UNAUTHORIZED);
//    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleThrowable(Throwable throwable) {
        String logCode = uuidGenerator.generateUUIDString();

        log.error("Unexpected error: '" + logCode + "'", throwable);

        ErrorDTO errorDTO = newErrorDTO()
                .status(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred. If this problem keeps persisting please contact our technical support with code: '" + logCode + "'.")
                .build();

        return new ResponseEntity<>(errorDTO, INTERNAL_SERVER_ERROR);
    }
}