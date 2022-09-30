package com.midasvision.midaslog.api.expceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.midasvision.midaslog.domain.exception.ServiceException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<ErrorBody.Campo> campos = new ArrayList<>();
		
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			String campo = ((FieldError)error).getField();
			String mensagem = error.getDefaultMessage();
			
			campos.add(new ErrorBody.Campo(campo, mensagem));
		}
		
		ErrorBody errorBody = new ErrorBody();
		errorBody.setStatus(status.value());
		errorBody.setData(LocalDateTime.now());
		errorBody.setTitulo("Um ou mais campos, apresentam erros no preenchimento");
		errorBody.setCampos(campos);
		
		return handleExceptionInternal(ex, errorBody, headers, status, request);
	}
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Object> handlerService(ServiceException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorBody errorBody = new ErrorBody();
		errorBody.setStatus(status.value());
		errorBody.setData(LocalDateTime.now());
		errorBody.setTitulo(ex.getMessage());
		
		
		return handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
	}
}
