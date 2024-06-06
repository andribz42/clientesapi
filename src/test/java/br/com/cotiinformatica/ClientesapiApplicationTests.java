package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.EnderecoDto;
import br.com.cotiinformatica.domain.dtos.ExcluirClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.ListarClienteResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientesapiApplicationTests {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	
	private static String clienteCpf;
	private static UUID clienteId;
	private static UUID enderecoId;
	
	private Faker faker;
	
	@BeforeEach
	void setUp() {
		faker = new Faker(Locale.forLanguageTag("pt-BR"));
	}
	
	@Test
	@Order(1)
	void shouldCreateUserSuccessfully() throws Exception {
		CriarClienteRequestDto request = new CriarClienteRequestDto();
		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setCpf(faker.number().digits(11));
		request.setDataNascimento(LocalDate.now());
		
		EnderecoDto enderecoDto = new EnderecoDto();
		enderecoDto.setBairro(faker.address().country());
		enderecoDto.setCep(faker.address().zipCode());
		enderecoDto.setCidade(faker.address().city());
		enderecoDto.setComplemento(faker.address().streetSuffix());
		enderecoDto.setLogradouro(faker.address().buildingNumber());
		enderecoDto.setNumero(faker.address().streetAddressNumber());
		enderecoDto.setUf(faker.address().stateAbbr());
		request.setEnderecos(Collections.singletonList(enderecoDto));
		
		MvcResult result = mockMvc.perform(post("/api/clientes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andReturn();
		
		CriarClienteResponseDto response = getResponse(result, CriarClienteResponseDto.class);
		
		assertNotNull(response);
		assertNotNull(response.getId());
		assertEquals(response.getEmail(), request.getEmail());
		assertEquals(response.getNome(), request.getNome());
		assertEquals(response.getCpf(), request.getCpf());
		assertEquals(response.getDataNascimento(), request.getDataNascimento());
		assertNotNull(response.getEnderecos());
		
		assertEquals(response.getEnderecos().get(0).getUf(), request.getEnderecos().get(0).getUf());
		assertEquals(response.getEnderecos().get(0).getBairro(), request.getEnderecos().get(0).getBairro());
		assertEquals(response.getEnderecos().get(0).getCep(), request.getEnderecos().get(0).getCep());
		assertEquals(response.getEnderecos().get(0).getCidade(), request.getEnderecos().get(0).getCidade());
		assertEquals(response.getEnderecos().get(0).getComplemento(), request.getEnderecos().get(0).getComplemento());
		assertEquals(response.getEnderecos().get(0).getLogradouro(), request.getEnderecos().get(0).getLogradouro());
		assertEquals(response.getEnderecos().get(0).getNumero(), request.getEnderecos().get(0).getNumero());
		assertNotNull(response.getEnderecos().get(0).getId());
		
		clienteCpf = response.getCpf();
		clienteId = response.getId();
		enderecoId = response.getEnderecos().get(0).getId();
	}
	
	@Test
	@Order(2)
	void shouldNotCreateUserAndReturnStatus422() throws Exception {
		CriarClienteRequestDto request = new CriarClienteRequestDto();
		request.setId(clienteId);
		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setCpf(clienteCpf);
		request.setDataNascimento(LocalDate.now());
		
		EnderecoDto enderecoDto = new EnderecoDto();
		enderecoDto.setId(enderecoId);
		enderecoDto.setBairro(faker.address().country());
		enderecoDto.setCep(faker.address().zipCode());
		enderecoDto.setCidade(faker.address().city());
		enderecoDto.setComplemento(faker.address().streetSuffix());
		enderecoDto.setLogradouro(faker.address().buildingNumber());
		enderecoDto.setNumero(faker.address().streetAddressNumber());
		enderecoDto.setUf(faker.address().stateAbbr());
		request.setEnderecos(Collections.singletonList(enderecoDto));
		
		mockMvc.perform(post("/api/clientes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}
	
	@Test
	@Order(3)
	void shouldUpdateUserSuccessfully() throws Exception {
		AtualizarClienteRequestDto request = new AtualizarClienteRequestDto();
		request.setId(clienteId);
		request.setCpf(clienteCpf);
		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setDataNascimento(LocalDate.now().minusDays(1));
		
		EnderecoDto enderecoDto = new EnderecoDto();
		enderecoDto.setId(enderecoId);
		enderecoDto.setBairro(faker.address().country());
		enderecoDto.setCep(faker.address().zipCode());
		enderecoDto.setCidade(faker.address().city());
		enderecoDto.setComplemento(faker.address().streetSuffix());
		enderecoDto.setLogradouro(faker.address().buildingNumber());
		enderecoDto.setNumero(faker.address().streetAddressNumber());
		enderecoDto.setUf(faker.address().stateAbbr());
		request.setEnderecos(Collections.singletonList(enderecoDto));
		
		MvcResult result = mockMvc.perform(put("/api/clientes/" + clienteId)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();
		
		AtualizarClienteResponseDto response = getResponse(result, AtualizarClienteResponseDto.class);
		
		assertNotNull(response);
		assertEquals(response.getId(), request.getId());
		assertEquals(response.getEmail(), request.getEmail());
		assertEquals(response.getNome(), request.getNome());
		assertEquals(response.getCpf(), request.getCpf());
		assertEquals(response.getDataNascimento(), request.getDataNascimento());
		assertNotNull(response.getEnderecos());

		assertEquals(response.getEnderecos().get(0).getId(), request.getEnderecos().get(0).getId());
		assertEquals(response.getEnderecos().get(0).getUf(), request.getEnderecos().get(0).getUf());
		assertEquals(response.getEnderecos().get(0).getBairro(), request.getEnderecos().get(0).getBairro());
		assertEquals(response.getEnderecos().get(0).getCep(), request.getEnderecos().get(0).getCep());
		assertEquals(response.getEnderecos().get(0).getCidade(), request.getEnderecos().get(0).getCidade());
		assertEquals(response.getEnderecos().get(0).getComplemento(), request.getEnderecos().get(0).getComplemento());
		assertEquals(response.getEnderecos().get(0).getLogradouro(), request.getEnderecos().get(0).getLogradouro());
		assertEquals(response.getEnderecos().get(0).getNumero(), request.getEnderecos().get(0).getNumero());
	}
	
	@Test
	@Order(4)
	void shouldListUserSuccessfully() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/clientes/" + clienteId)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		ListarClienteResponseDto response = getResponse(result, ListarClienteResponseDto.class);
		
		assertNotNull(response);
		assertEquals(response.getId(), clienteId);
		assertNotNull(response.getEmail());
		assertNotNull(response.getNome());
		assertNotNull(response.getCpf());
		assertNotNull(response.getDataNascimento());
		assertNotNull(response.getEnderecos());

		assertNotNull(response.getEnderecos().get(0).getId());
		assertNotNull(response.getEnderecos().get(0).getUf());
		assertNotNull(response.getEnderecos().get(0).getBairro());
		assertNotNull(response.getEnderecos().get(0).getCep());
		assertNotNull(response.getEnderecos().get(0).getCidade());
		assertNotNull(response.getEnderecos().get(0).getComplemento());
		assertNotNull(response.getEnderecos().get(0).getLogradouro());
		assertNotNull(response.getEnderecos().get(0).getNumero());
	}
	
	@Test
	@Order(4)
	void shouldDeleteUserSuccessfully() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/clientes/" + clienteId)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		ExcluirClienteResponseDto response = getResponse(result, ExcluirClienteResponseDto.class);
		
		assertNotNull(response);
		assertEquals(response.getId(), clienteId);
		assertEquals(response.getCpf(), clienteCpf);
		assertNotNull(response.getEmail());
		assertNotNull(response.getNome());
		assertNotNull(response.getDataNascimento());
		assertNotNull(response.getEnderecos());

		assertEquals(response.getEnderecos().get(0).getId(), enderecoId);
		assertNotNull(response.getEnderecos().get(0).getId());
		assertNotNull(response.getEnderecos().get(0).getUf());
		assertNotNull(response.getEnderecos().get(0).getBairro());
		assertNotNull(response.getEnderecos().get(0).getCep());
		assertNotNull(response.getEnderecos().get(0).getCidade());
		assertNotNull(response.getEnderecos().get(0).getComplemento());
		assertNotNull(response.getEnderecos().get(0).getLogradouro());
		assertNotNull(response.getEnderecos().get(0).getNumero());
	}
	
	@Test
	@Order(5)
	void shouldNotListDeletedUser() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/clientes/" + clienteId)
				.contentType("application/json"))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andReturn();
		
		assertEquals(result.getResolvedException().getLocalizedMessage(), "O cliente " + clienteId + " informado não está cadastrado, tente outro.");
	}
	
	@Test
	@Order(6)
	void shouldListUsersSuccessfully() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/clientes")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		Object response = getResponse(result, Object.class);
		
		assertNotNull(response);
	}

	private <T> T getResponse(MvcResult result, Class<T> responseType) throws Exception {
		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		return objectMapper.readValue(content, responseType);
	}
}
