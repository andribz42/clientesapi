package br.com.cotiinformatica.application.handlers;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.cotiinformatica.domain.exceptions.AddressNotRegisteredException;
import br.com.cotiinformatica.domain.exceptions.ClientNotRegisteredException;
import br.com.cotiinformatica.domain.exceptions.ClientsNotRegisteredException;
import br.com.cotiinformatica.domain.exceptions.CpfAlreadyRegisteredException;
@ControllerAdvice
public class UnprocessableEntityHandler {
	
	@ExceptionHandler(CpfAlreadyRegisteredException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<String> errorHandler(CpfAlreadyRegisteredException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
	
	@ExceptionHandler(ClientNotRegisteredException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<String> errorHandler(ClientNotRegisteredException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}

	@ExceptionHandler(ClientsNotRegisteredException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<String> errorHandler(ClientsNotRegisteredException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
	
	@ExceptionHandler(AddressNotRegisteredException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public List<String> errorHandler(AddressNotRegisteredException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
}
