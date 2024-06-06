package br.com.cotiinformatica.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EnderecoDto {

	private UUID id;
	
	@NotEmpty(message = "O preenchimento do logradouro é obrigatório.")
	private String logradouro;
	private String complemento;
	private String numero;
	
	@NotEmpty(message = "O preenchimento do bairro é obrigatório.")
	private String bairro;
	
	@NotEmpty(message = "O preenchimento da cidade é obrigatório.")
	private String cidade;
	
	@NotEmpty(message = "O preenchimento da uf é obrigatório.")
	@Pattern(regexp = "^[A-Z]{2}$", message = "Formato da UF inválido. Utilizar o padrão 'XX'.")
	private String uf;
	
	@NotEmpty(message = "O preenchimento do cep é obrigatório.")
	@Pattern(regexp = "^\\d{7}$")
	private String cep;
}
