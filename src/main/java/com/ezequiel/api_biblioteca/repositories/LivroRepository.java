package com.ezequiel.api_biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezequiel.api_biblioteca.models.LivroModel;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Integer> {
    
}
