package com.biblioteca.api_biblioteca.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idEmprestimo;
    
    @ManyToOne
    @JoinColumn(name = "idPessoa")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "idLivro")
    private Livro livro;
}
