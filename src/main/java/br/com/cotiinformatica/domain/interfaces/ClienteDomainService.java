package br.com.cotiinformatica.domain.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ExcluirClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ListarClienteResponseDto;

public interface ClienteDomainService {
	
	public CriarClienteResponseDto cadastrarCliente(CriarClienteRequestDto request);
	public AtualizarClienteResponseDto atualizarCliente(UUID id, AtualizarClienteRequestDto request);
	public ExcluirClienteResponseDto excluirCliente(UUID id);
	public List<ListarClienteResponseDto> listarClientes();
	public ListarClienteResponseDto listarCliente(UUID id);
	
}
