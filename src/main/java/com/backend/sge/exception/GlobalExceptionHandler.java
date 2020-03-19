package com.backend.sge.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException(BadRequestException badRequestException,
                                                        WebRequest webRequest) {
        RestError restError = new RestError(new Date(), badRequestException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(NotFoundException notFoundException, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), notFoundException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlerConstraintViolationException(
            ConstraintViolationException constraintViolationException, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), constraintViolationException.getSQLException().getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handlerAccessDeniedException(AccessDeniedException accessDeniedException,
//                                                          WebRequest webRequest) {
//        RestError restError = new RestError(new Date(), accessDeniedException.getMessage(),
//                webRequest.getDescription(false));
//        return new ResponseEntity<>(restError, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception exception, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(restError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        List<RestError> restErrors = new ArrayList<RestError>();
        List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            RestError restError = new RestError(new Date(), "Erro de Validação", fieldError.getDefaultMessage());
            restErrors.add(restError);
        }
        return new ResponseEntity<>(restErrors, httpStatus);

    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException asyncRequestTimeoutException, HttpHeaders httpHeaders, HttpStatus httpStatus,
            WebRequest webRequest) {
        RestError restError = new RestError(new Date(), asyncRequestTimeoutException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException bindException, HttpHeaders httpHeaders,
                                                         HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), bindException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException conversionNotSupportedException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), conversionNotSupportedException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object object,
                                                             HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), httpMediaTypeNotAcceptableException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), httpMediaTypeNotSupportedException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException httpMessageNotReadableException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), httpMessageNotReadableException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException httpMessageNotWritableException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), httpMessageNotWritableException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), httpRequestMethodNotSupportedException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException missingPathVariableException, HttpHeaders httpHeaders, HttpStatus httpStatus,
            WebRequest webRequest) {
        RestError restError = new RestError(new Date(), missingPathVariableException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException missingServletRequestParameterException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), missingServletRequestParameterException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException missingServletRequestPartException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), missingServletRequestPartException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException,
                                                                   HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), noHandlerFoundException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException servletRequestBindingException, HttpHeaders httpHeaders,
            HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), servletRequestBindingException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException typeMismatchException,
                                                        HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
        RestError restError = new RestError(new Date(), typeMismatchException.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(restError, httpStatus);
    }

}