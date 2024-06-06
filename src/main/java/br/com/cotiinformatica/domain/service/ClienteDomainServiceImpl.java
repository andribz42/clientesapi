package br.com.cotiinformatica.domain.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.EnderecoDto;
import br.com.cotiinformatica.domain.dtos.ExcluirClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ListarClienteResponseDto;
import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.entities.Endereco;
import br.com.cotiinformatica.domain.exceptions.ClientNotRegisteredException;
import br.com.cotiinformatica.domain.exceptions.ClientsNotRegisteredException;
import br.com.cotiinformatica.domain.exceptions.CpfAlreadyRegisteredException;
import br.com.cotiinformatica.domain.interfaces.ClienteDomainService;
import br.com.cotiinformatica.insfrastructure.components.RabbitMQProducerComponent;
import br.com.cotiinformatica.insfrastructure.repositories.ClienteRepository;
import br.com.cotiinformatica.insfrastructure.repositories.EnderecoRepository;

@Service
public class ClienteDomainServiceImpl implements ClienteDomainService {

	@Autowired ModelMapper modelMapper;
	@Autowired ClienteRepository clienteRepository;
	@Autowired EnderecoRepository enderecoRepository;
	@Autowired RabbitMQProducerComponent rabbitMQProducerComponent;
	
	@Override
	public CriarClienteResponseDto cadastrarCliente(CriarClienteRequestDto request) {
		if(clienteRepository.findByCPF(request.getCpf()) != null) {
			throw new CpfAlreadyRegisteredException(request.getCpf());
		}
		
		Cliente cliente = modelMapper.map(request, Cliente.class);
		cliente.setId(UUID.randomUUID());
		cliente.setDataNascimento(getDateValue(request.getDataNascimento()));
		clienteRepository.save(cliente);
		
		ArrayList<Endereco> enderecos = new ArrayList<Endereco>();
		
		request.getEnderecos().forEach(enderecoRequest -> {
			Endereco endereco = modelMapper.map(enderecoRequest, Endereco.class);
			endereco.setId(UUID.randomUUID());
			endereco.setClienteId(cliente.getId());
			enderecoRepository.save(endereco);
			enderecos.add(endereco);
		});
		
		try {
			rabbitMQProducerComponent.sendMessage(cliente);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CriarClienteResponseDto response = modelMapper.map(cliente, CriarClienteResponseDto.class);
		response.setDataNascimento(getLocalDateValue(cliente.getDataNascimento()));
		response.setEnderecos(new ArrayList<EnderecoDto>());
		enderecos.forEach(endereco -> {
			response.getEnderecos().add(modelMapper.map(endereco, EnderecoDto.class));
		});
		
		return response;
	}

	@Override
	public AtualizarClienteResponseDto atualizarCliente(UUID clientId, AtualizarClienteRequestDto request) {
		if(clienteRepository.findById(clientId) == null) {
			throw new ClientNotRegisteredException(clientId);
		}
		
		Cliente cliente = modelMapper.map(request, Cliente.class);
		cliente.setId(clientId);
		cliente.setDataNascimento(getDateValue(request.getDataNascimento()));
		clienteRepository.save(cliente);
		
		ArrayList<Endereco> enderecos = new ArrayList<Endereco>();
		
		request.getEnderecos().forEach(enderecoRequest -> {
			Endereco endereco = modelMapper.map(enderecoRequest, Endereco.class);
			endereco.setClienteId(cliente.getId());
			
			if(endereco.getId() == null) {
				endereco.setId(UUID.randomUUID());
			}
			
			enderecoRepository.save(endereco);
			enderecos.add(endereco);
		});
		
		AtualizarClienteResponseDto response = modelMapper.map(cliente, AtualizarClienteResponseDto.class);
		response.setDataNascimento(getLocalDateValue(cliente.getDataNascimento()));
		response.setEnderecos(new ArrayList<EnderecoDto>());
		enderecos.forEach(endereco -> {
			response.getEnderecos().add(modelMapper.map(endereco, EnderecoDto.class));
		});
		
		return response;
	}

	@Override
	public ExcluirClienteResponseDto excluirCliente(UUID clientId) {
		Cliente cliente = clienteRepository.findById(clientId).orElse(null);
		if(cliente == null) {
			throw new ClientNotRegisteredException(clientId);
		}
		
		List<Endereco> enderecos = enderecoRepository.findByClientId(clientId);
		
		ExcluirClienteResponseDto response = modelMapper.map(cliente, ExcluirClienteResponseDto.class);
		response.setDataNascimento(getLocalDateValue(cliente.getDataNascimento()));
		response.setEnderecos(new ArrayList<EnderecoDto>());
		enderecos.forEach(endereco -> {
			response.getEnderecos().add(modelMapper.map(endereco, EnderecoDto.class));
		});
		
		clienteRepository.delete(cliente);
		enderecoRepository.deleteAll(enderecos);
		
		return response;
	}

	@Override
	public List<ListarClienteResponseDto> listarClientes() {
		List<Cliente> clientes = clienteRepository.findAll();
		if(clientes.isEmpty()) {
			throw new ClientsNotRegisteredException();
		}
		
		Collections.sort(clientes, Comparator.comparing(Cliente::getNome));
		
		List<ListarClienteResponseDto> response = new ArrayList<ListarClienteResponseDto>(); 
		
		clientes.forEach(cliente -> {
			List<Endereco> enderecos = enderecoRepository.findByClientId(cliente.getId());
			
			ListarClienteResponseDto clientesDto = modelMapper.map(cliente, ListarClienteResponseDto.class);
			clientesDto.setDataNascimento(getLocalDateValue(cliente.getDataNascimento()));
			clientesDto.setEnderecos(new ArrayList<EnderecoDto>());
			enderecos.forEach(endereco -> {
				clientesDto.getEnderecos().add(modelMapper.map(endereco, EnderecoDto.class));
			});
			response.add(clientesDto);
		});
		
		return response;
	}

	@Override
	public ListarClienteResponseDto listarCliente(UUID clientId) {
		Cliente cliente = clienteRepository.findById(clientId).orElse(null);
		if(cliente == null) {
			throw new ClientNotRegisteredException(clientId);
		}
		List<Endereco> enderecos = enderecoRepository.findByClientId(clientId);
		ListarClienteResponseDto response = modelMapper.map(cliente, ListarClienteResponseDto.class);
		response.setDataNascimento(getLocalDateValue(cliente.getDataNascimento()));
		response.setEnderecos(new ArrayList<EnderecoDto>());
		enderecos.forEach(endereco -> {
			response.getEnderecos().add(modelMapper.map(endereco, EnderecoDto.class));
		});
		
		return response;
	}
	
	public Date getDateValue(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public LocalDate getLocalDateValue(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
