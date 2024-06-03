package com.jbl.t24.rest.api.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import java.util.ArrayList;

//import java.util.List;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
//import com.jbl.t24.rest.api.enums.ResponseStatus;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.enums.ResponseStatus;

@RestControllerAdvice
public class GlobalCtlrAdvice extends ResponseEntityExceptionHandler {

	Logger logger = LogManager.getLogger(GlobalCtlrAdvice.class);

	// @ExceptionHandler(NullPointerException.class)
	// public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest
	// request) {
	//
	// JwtErrorResponse jwtErrorResponse = new
	// JwtErrorResponse(HttpStatus.BAD_REQUEST,
	// ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue());
	// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
	// }
	//
	// @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	// public ResponseEntity<?> globalExceptionHandlerMediaType(Exception ex,
	// WebRequest request) {
	//
	// JwtErrorResponse jwtErrorResponse = new
	// JwtErrorResponse(HttpStatus.FORBIDDEN, ResponseStatus.FOURZ15.getText(),
	// ResponseStatus.FOURZ15.getValue());
	//
	// return ResponseEntity.status(HttpStatus.FORBIDDEN).body(jwtErrorResponse);
	// }
	//
	// @ExceptionHandler(HttpMessageNotReadableException.class)
	// public ResponseEntity<?> globalExceptionHandlerReadable(Exception ex,
	// WebRequest request) {
	//
	// JwtErrorResponse jwtErrorResponse = new
	// JwtErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
	// ResponseStatus.FOURZ16.getText(), ResponseStatus.FOURZ16.getValue());
	//
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jwtErrorResponse);
	// }
	//
	// @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	// public ResponseEntity<?>
	// globalExceptionHandlerReadableRequestMethod(Exception ex, WebRequest request)
	// {
	//
	// JwtErrorResponse jwtErrorResponse = new
	// JwtErrorResponse(HttpStatus.FORBIDDEN, ResponseStatus.FOURZ5.getText(),
	// ResponseStatus.FOURZ5.getValue());
	//
	// return ResponseEntity.status(HttpStatus.FORBIDDEN).body(jwtErrorResponse);
	// }

	// 400
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.info(ex.getClass().getName());
		logger.error("error", ex);

		List<String> errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

		JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, errors);
		return handleExceptionInternal(ex, jwtErrorResponse, headers, jwtErrorResponse.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, errors);
		return handleExceptionInternal(ex, jwtErrorResponse, headers, jwtErrorResponse.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, error);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final String error = ex.getRequestPartName() + " part is missing";
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, error);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final String error = ex.getParameterName() + " parameter is missing";
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, error);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, error);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final List<String> errors = new ArrayList<String>();
		for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ0.getText(), ResponseStatus.FOURZ0.getValue(), timestamp, errors);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	// 404

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.NOT_FOUND,
				ResponseStatus.FOURZ4.getText(), ResponseStatus.FOURZ4.getValue(), timestamp, error);
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	// 405

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request.");
		// ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
				ResponseStatus.FOURZ5.getText(), ResponseStatus.FOURZ5.getValue(), timestamp, builder.toString());
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	// 415

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. ");
		// ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		// final JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
		// ResponseStatus.FOURZ15.getText(), ResponseStatus.FOURZ15.getValue(),
		// timestamp,
		// builder.substring(0, builder.length() - 2));
		// return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(),
		// jwtErrorResponse.getStatus());

		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				ErrorMessageGenerator(ex).toString(), ResponseStatus.FIVEZ0.getValue(), timestamp,
				ErrorMessageGenerator(ex));
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

	private List<String> ErrorMessageGenerator(Exception e) {
		String errorMessage = e.getStackTrace()[0].getFileName() + " " + e.getStackTrace()[0].getLineNumber()
				+ " " + e.getMessage();
		List<String> errorList = new ArrayList<>();
		errorList.add(errorMessage);

		return errorList;
	}

	// 500

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		

		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getMessage(), ResponseStatus.FIVEZ0.getValue(), timestamp, ErrorMessageGenerator(ex));

		logger.error("Global Control Error:: " + ErrorMessageGenerator(ex).toString());
		ex.printStackTrace();
		return new ResponseEntity<Object>(jwtErrorResponse, new HttpHeaders(), jwtErrorResponse.getStatus());
	}

}
