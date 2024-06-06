package br.com.cotiinformatica.domain.exceptions;

public class CpfAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CpfAlreadyRegisteredException(String cpf) {
		super("O cpf " + cpf + " informado já está cadastrado, tente outro.");
	}
	
}
