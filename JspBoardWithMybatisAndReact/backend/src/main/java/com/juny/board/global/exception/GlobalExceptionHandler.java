package com.juny.board.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   *
   *
   * <h1>컨트롤러 Bean Validation 에러 처리</h1>
   *
   * @param e
   * @return ErrorResponse
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    String defaultMessage =
        e.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(fieldError -> fieldError.getDefaultMessage())
            .orElse(ErrorMessage.VALIDATION_FAILED_MSG);

    return new ResponseEntity<>(
        new ErrorResponse(ErrorCode.REQUEST_PARAMETER_INVALID, defaultMessage),
        ErrorCode.REQUEST_PARAMETER_INVALID.getHttpStatus());
  }

  /**
   *
   *
   * <h1>비지니스 예외를 상속받은 클래스 처리</h1>
   *
   * @param e
   * @return ErrorResponse
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

    return new ResponseEntity<>(
        new ErrorResponse(e.getErrorCode()), e.getErrorCode().getHttpStatus());
  }

  /**
   *
   *
   * <h1>비즈니스 예외가 아닌 것은 서버 에러(500)로 처리</h1>
   *
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handle(Exception e) {

    e.printStackTrace();

    return new ResponseEntity<>(
        new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR),
        ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
  }
}
