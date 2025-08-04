package com.biblioteca.api_biblioteca.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.api_biblioteca.data.entity.Livro;
import java.util.Optional;


@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

    boolean existsByNomeAndAutorAndDataLancamento(String nome, String autor, LocalDate dataLancamento);
    Optional<Livro> findByNomeAndAutorAndDataLancamento (String nome, String autor, LocalDate dataLancamnto);
}
