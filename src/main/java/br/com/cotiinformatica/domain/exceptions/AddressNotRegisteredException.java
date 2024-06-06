package br.com.cotiinformatica.domain.exceptions;

import java.util.UUID;

public class AddressNotRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AddressNotRegisteredException(UUID id) {
		super("O endereço " + id + " informado não está cadastrado, tente outro.");
	}
	
}
