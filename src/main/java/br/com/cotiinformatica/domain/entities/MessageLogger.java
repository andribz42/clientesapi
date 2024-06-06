package br.com.cotiinformatica.domain.entities;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class MessageLogger {
	@Id
	private UUID id;
	private String status;
	private Instant createdAt;
	private String message;
	
	@DBRef
	private Cliente cliente;
}
