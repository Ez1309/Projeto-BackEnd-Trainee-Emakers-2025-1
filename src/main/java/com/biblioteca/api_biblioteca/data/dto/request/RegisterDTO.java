package com.biblioteca.api_biblioteca.data.dto.request;

import com.biblioteca.api_biblioteca.data.entity.PessoaRole;

public record RegisterDTO (String email, String senha, PessoaRole role){
    
}
