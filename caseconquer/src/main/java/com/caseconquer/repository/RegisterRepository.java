package com.caseconquer.repository;

import org.springframework.data.repository.CrudRepository;

import com.caseconquer.models.Register;

public interface RegisterRepository extends CrudRepository<Register, String>{
	Register findByCodigo(long codigo);
	boolean existsByEmail(String email);
	boolean existsByCpf(String cpf);
}