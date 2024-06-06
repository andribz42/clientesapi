package br.com.cotiinformatica.domain.exceptions;

import java.util.UUID;

public class ClientNotRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClientNotRegisteredException(UUID id) {
		super("O cliente " + id + " informado não está cadastrado, tente outro.");
	}
	
}
