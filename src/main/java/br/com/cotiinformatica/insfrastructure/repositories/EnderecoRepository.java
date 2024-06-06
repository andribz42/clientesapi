package br.com.cotiinformatica.insfrastructure.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Endereco;

@Repository
public interface EnderecoRepository extends MongoRepository<Endereco, UUID> {
}
