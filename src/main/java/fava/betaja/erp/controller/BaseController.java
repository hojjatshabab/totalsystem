package fava.betaja.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;



public abstract class BaseController {


    @Autowired
    private MessageSource messageSource;
//    @Value("${app.devMode}")
//    private Boolean devMode;

    public <T> ActionResult<T> RESULT(T data, Locale locale) {
        String message = messageSource.getMessage("info.ok", null, locale);
        return new ActionResult<>(data, 200, message);
    }

    public <T> ActionResult<T> INTERNAL_SERVER_ERROR(String msg, Locale locale) {
        String message = messageSource.getMessage("error.server_error", null, locale);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public <T> ActionResult<T> GONE(String msg, Locale locale) {
        String message = messageSource.getMessage("error.gone", null, locale);
        throw new ResponseStatusException(HttpStatus.GONE,"{" + msg +"}"+ message);
    }
    public <T> ActionResult<T> LENGTH_REQUIRED(String msg, Locale locale) {
        String message = messageSource.getMessage("error.length_required", null, locale);
        throw new ResponseStatusException(HttpStatus.LENGTH_REQUIRED,"{" + msg +"}"+ message);
    }

    public <T> ActionResult<T> NOT_ACCEPTABLE(String msg, Locale locale) {
        String message = messageSource.getMessage("error.not_acceptable", null, locale);
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, " {" + msg + "} " + message);
    }
    public <T> ActionResult<T> NOT_ACCEPTABLE(String msg, String secondMsg, Locale locale) {
        String message = messageSource.getMessage("error.not_acceptable", null, locale);
        String secondMessage = messageSource.getMessage(secondMsg, null, locale);
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, " {" + msg + "} " + secondMessage + "  " + message);
    }

    public <T> ActionResult<T> NOT_FOUND(String msg, Locale locale) {
        String message = messageSource.getMessage("error.notfound", null, locale);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, " {" + msg + "} " + message);
    }

    public <T> ActionResult<T> NO_CONTENT(String msg, Locale locale) {
        String message = messageSource.getMessage("error.no_content", null, locale);
        throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, " {" + msg + "} " + message);
    }
    public <T> ActionResult<T> NO_CONTENT_204(String msg, Locale locale) {
        String message = messageSource.getMessage("error.no_content", null, locale);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, " {" + msg + "} " + message);
    }

    public <T> ActionResult<T> CONFLICT(String msg, Locale locale) {
        String message = messageSource.getMessage("error.conflict", null, locale);
        throw new ResponseStatusException(HttpStatus.CONFLICT, " {" + msg + "} " + message);
    }

    public <T> ActionResult<T> UNAUTHORIZED(String msg, Locale locale) {
        String message = messageSource.getMessage("error.UNAUTHORIZED", null, locale);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " {" + msg + "} " + message);
    }
    public <T> ActionResult<T> PAYMENT_REQUIRED(String msg, Locale locale) {
        String message = messageSource.getMessage("error.PAYMENT_REQUIRED", null, locale);
        throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, " {" + msg + "} " + message);
    }





    public <T> ActionResult<T> UNAUTHORIZED() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    }

    public <T> ActionResult<T> USER_LOCKED() {
        throw new ResponseStatusException(HttpStatus.LOCKED, "user_locked");
    }

    public <T> ActionResult<T> FORBIDDEN() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "forbidden");
    }
}
