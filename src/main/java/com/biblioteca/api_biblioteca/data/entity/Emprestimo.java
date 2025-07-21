package com.biblioteca.api_biblioteca.data.entity;

import com.biblioteca.api_biblioteca.data.dto.request.EmprestimoRequestDTO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
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

    public Emprestimo(EmprestimoRequestDTO emprestimoRequestDTO){
        this.pessoa = emprestimoRequestDTO.pessoa();
        this.livro = emprestimoRequestDTO.livro();
    }

    
}
