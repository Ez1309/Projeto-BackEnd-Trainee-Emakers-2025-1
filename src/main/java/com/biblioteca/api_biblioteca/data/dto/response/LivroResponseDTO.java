package com.biblioteca.api_biblioteca.data.dto.response;

import com.biblioteca.api_biblioteca.data.entity.Livro;

public record LivroResponseDTO(

    Long id,

    String name
){
    public LivroResponseDTO(Livro livro){
        this(livro.getIdLivro(), livro.getNome());
    }
}
