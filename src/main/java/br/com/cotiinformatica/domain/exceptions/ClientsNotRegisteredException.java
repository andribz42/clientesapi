package br.com.cotiinformatica.domain.exceptions;

public class ClientsNotRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClientsNotRegisteredException() {
		super("Nenhum cliente está cadastrado no sistema.");
	}
	
}
