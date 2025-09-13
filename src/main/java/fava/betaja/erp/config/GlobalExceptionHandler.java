package fava.betaja.erp.config;

import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.handlers.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ======================= ServiceException =======================
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException ex) {
        log.error("ServiceException در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("خطای سرویس")
                .message(ex.getMessage())
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======================= NullPointerException =======================
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(HttpServletRequest request, NullPointerException ex) {
        log.error("NullPointerException در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("اشتباه در داده‌ها")
                .message("مقداری که انتظار می‌رفت وجود ندارد")
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======================= IllegalArgumentException =======================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(HttpServletRequest request, IllegalArgumentException ex) {
        log.error("IllegalArgumentException در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("ورودی نامعتبر")
                .message("مقدار ورودی نامعتبر است: " + ex.getMessage())
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======================= MethodArgumentTypeMismatchException =======================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        log.error("TypeMismatch در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        String message = String.format("پارامتر '%s' دارای مقدار نامعتبر '%s' است", ex.getName(), ex.getValue());

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("نوع پارامتر نامعتبر")
                .message(message)
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======================= MethodArgumentNotValidException =======================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error("ValidationException در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        String messages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("اعتبارسنجی ناموفق")
                .message("لطفاً داده‌های ورودی را بررسی کنید: " + messages)
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ======================= DataIntegrityViolationException =======================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(HttpServletRequest request, DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolation در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("تداخل داده‌ای")
                .message("عملیات جاری با داده‌های موجود در پایگاه داده تداخل دارد: " + ex.getMostSpecificCause().getMessage())
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // ======================= Exception عمومی =======================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(HttpServletRequest request, Exception ex) {
        log.error("خطای ناشناخته در مسیر {}: {}", request.getServletPath(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("خطای سرور داخلی")
                .message("یک خطای غیرمنتظره در سرور رخ داده است")
                .path(request.getServletPath())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
