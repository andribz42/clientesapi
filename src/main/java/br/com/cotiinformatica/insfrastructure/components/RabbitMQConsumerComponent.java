package br.com.cotiinformatica.insfrastructure.components;

import java.time.Instant;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.entities.MessageLogger;
import br.com.cotiinformatica.insfrastructure.repositories.MessageLoggerRepository;

@Component
public class RabbitMQConsumerComponent {

	@Autowired MessageLoggerRepository messageLoggerRepository;
	@Autowired MailMessageComponent mailMessageComponent;
	@Autowired ObjectMapper objectMapper;
	
	@RabbitListener(queues = {"${queue.name}"})
	public void proccessMessage(@Payload String message) {
		
		MessageLogger messageLogger = new MessageLogger();
		messageLogger.setId(UUID.randomUUID());
		messageLogger.setCreatedAt(Instant.now());
		
		try {
			Cliente user = objectMapper.readValue(message, Cliente.class);
			
			String to = user.getEmail();
			String subject = "O cadastro foi realizado com sucesso!";
			String body = "Olá, sua conta foi cadastrada com sucesso!\n\nAt.te.\nEquipe";
			
			mailMessageComponent.send(to, subject, body);
			messageLogger.setCliente(user);
			messageLogger.setStatus("SUCESSO");
			messageLogger.setMessage("Email de boas vindas enviado com sucesso para: " + user.getEmail());
		} catch (Exception e) {
			messageLogger.setStatus("ERROR");
			messageLogger.setMessage(e.getMessage());
		} finally {
			messageLoggerRepository.save(messageLogger);
		}
	}
}
