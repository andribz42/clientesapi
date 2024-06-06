package br.com.cotiinformatica.application.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ExcluirClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ListarClienteResponseDto;
import br.com.cotiinformatica.domain.interfaces.ClienteDomainService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	@Autowired ClienteDomainService domainService;
	
	@PostMapping
	public ResponseEntity<CriarClienteResponseDto> cadastrarCliente(@RequestBody CriarClienteRequestDto request) {
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(domainService.cadastrarCliente(request));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<AtualizarClienteResponseDto> atualizarCliente(
			@PathVariable("id") UUID clientId, 
			@RequestBody AtualizarClienteRequestDto request) {
		return ResponseEntity.ok(domainService.atualizarCliente(clientId, request));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ExcluirClienteResponseDto> excluirCliente(@PathVariable("id") UUID clientId) {
		return ResponseEntity.ok(domainService.excluirCliente(clientId));
	}
	
	@GetMapping
	public ResponseEntity<List<ListarClienteResponseDto>> listarClientes() {
		return ResponseEntity.ok(domainService.listarClientes());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ListarClienteResponseDto> listarCliente(@PathVariable("id") UUID clientId) {
		return ResponseEntity.ok(domainService.listarCliente(clientId));
	}


}
