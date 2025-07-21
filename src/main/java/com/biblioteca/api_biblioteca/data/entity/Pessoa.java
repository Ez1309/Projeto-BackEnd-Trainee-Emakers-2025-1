package com.biblioteca.api_biblioteca.data.entity;

import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idPessoa;

    @Column(name="nome", nullable = false, length = 100)
    private String nome;

    @Column(name="cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name="cep", nullable = false, length = 9)
    private String cep;

    @Column(name="email", nullable = false, length = 100)
    private String email;

    @Column(name="senha", nullable = false, length = 100)
    private String senha;

    public Pessoa(PessoaRequestDTO pessoaRequestDTO){
        this.nome = pessoaRequestDTO.nome();
        this.cpf = pessoaRequestDTO.cpf();
        this.cep = pessoaRequestDTO.cep();
        this.email = pessoaRequestDTO.email();
        this.senha = pessoaRequestDTO.senha();
    }

}
