package br.com.cotiinformatica.insfrastructure.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, UUID> {
	
	@Query("{ 'cpf' : ?0 }")
	public Cliente findByCPF(String cpf);
}
