package com.teamapt.offlinesales.testing.app.config;

import com.teamapt.offlinesales.testing.app.model.response.ResponseModel;
import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceBadRequestException;
import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceException;
import com.teamapt.offlinesales.testing.domain.exceptions.UserServiceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

import static com.teamapt.offlinesales.testing.domain.utils.ErrorFormatter.format;
import static com.teamapt.offlinesales.testing.domain.utils.FunctionUtil.emptyIfNullStream;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice extends ResponseEntityExceptionHandler implements ResponseBodyAdvice<Object> {

    private static final String ERROR_MESSAGE = "Unhandled exception encountered: {}";

    @ExceptionHandler(value = {FileUploadException.class, NoSuchElementException.class})
    public ResponseEntity<Object> handleUploadException(Exception ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {CompletionException.class})
    public ResponseEntity<Object> handleCompletionException(CompletionException ex) {
        if (ex.getCause() instanceof UserServiceException userServiceException) {
            return handleDomainException((UserServiceException) userServiceException.getCause());
        }
        return handleException(ex);
    }

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleDomainException(UserServiceException ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {UserServiceBadRequestException.class})
    public ResponseEntity<Object> handleDomainBadRequestException(UserServiceBadRequestException ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), BAD_REQUEST.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {UserServiceNotFoundException.class})
    public ResponseEntity<Object> handleDomainNotFoundException(UserServiceNotFoundException ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), NOT_FOUND.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", format(ex.getConstraintViolations()), BAD_REQUEST.value()).toResponseEntity();
    }


    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), FORBIDDEN.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {MultipartException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        String msg = String.format("Exception message [%s] %n Metadata [%s]",
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseModel<>(null, "", singletonList(msg), BAD_REQUEST.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), BAD_REQUEST.value()).toResponseEntity();
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Object> handleException(Throwable ex) {
        log.error(ERROR_MESSAGE, ex.getMessage(), ex);
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleConverterErrors(MethodArgumentTypeMismatchException ex) {
        if (ex.getCause() instanceof IllegalArgumentException) {
            return handleIllegalArgumentException((IllegalArgumentException) ex.getCause());
        }
        return handleException(ex);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatusCode status,
                                                         WebRequest request) {
        return new ResponseModel<>(null, "", extractErrorMessages(ex.getFieldErrors()), BAD_REQUEST.value()).toResponseEntity();
    }

    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ResponseModel<>(null, "", format(bindingResult), BAD_REQUEST.value()).toResponseEntity();
    }

    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return new ResponseModel<>(null, "", singletonList(ex.getMessage()), BAD_REQUEST.value()).toResponseEntity();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();

        if (HttpStatus.valueOf(httpResponse.getStatus())
                .is2xxSuccessful()) {
            return new ResponseModel<>(body);
        }

        return body;
    }

    private List<String> extractErrorMessages(List<FieldError> fielderrors) {
        return emptyIfNullStream(fielderrors)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}

