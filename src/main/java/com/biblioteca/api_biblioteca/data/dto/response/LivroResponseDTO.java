package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.fasterxml.jackson.annotation.JsonFormat;

public record LivroResponseDTO(

    Long id,

    String name,

    String autor,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataLancamento,

    Boolean disponivel
){
    public LivroResponseDTO(Livro livro){
        this(livro.getIdLivro(), livro.getNome(), livro.getAutor(), livro.getDataLancamento(), livro.getDisponivel());
    }
}
