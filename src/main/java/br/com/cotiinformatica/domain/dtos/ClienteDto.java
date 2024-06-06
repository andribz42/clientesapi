package br.com.cotiinformatica.domain.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Data;

@Data
public class ClienteDto {

	private UUID id;
	
	@NotEmpty(message = "O preenchimento do nome é obrigatório.")
	@Length(min = 8, max = 100)
	private String nome;
	
	@NotEmpty(message = "O preenchimento do email é obrigatório.")
	@Email(message = "Informe um endereço de email válido.")
	private String email;
	
	@NotEmpty(message = "O preenchimento do cpf é obrigatório.")
	@Pattern(regexp = "^\\d{11}$", message = "O formato do cpf é inválido.")
	private String cpf;
	
	@NotEmpty(message = "O preenchimento da data de nascimento é obrigatória.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", flags = Flag.CASE_INSENSITIVE, 
    message = "O formato da data de nascimento é inválido. Utilize o formado 'yyyy-MM-dd'.")
	private LocalDate dataNascimento;
	
	private List<EnderecoDto> enderecos;
	
}
