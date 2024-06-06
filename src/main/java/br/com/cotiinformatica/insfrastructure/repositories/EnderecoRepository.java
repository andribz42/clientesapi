package br.com.cotiinformatica.insfrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Endereco;

@Repository
public interface EnderecoRepository extends MongoRepository<Endereco, UUID> {
	@Query("{ 'clienteId' : ?0 }")
	public List<Endereco> findByClientId(UUID clientId);
}
