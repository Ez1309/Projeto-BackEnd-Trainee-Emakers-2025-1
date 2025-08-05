package com.biblioteca.api_biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.api_biblioteca.data.entity.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByEmail(String email);

    Optional<Pessoa> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
