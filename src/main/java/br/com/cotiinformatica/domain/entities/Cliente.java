package br.com.cotiinformatica.domain.entities;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Cliente {

	@Id
	private UUID id;
	
	private String nome;
	
	@Indexed(unique = true)
	private String email;
	
	private String cpf;
	
	private Date dataNascimento;
}
