package com.biblioteca.api_biblioteca.data.entity;

import java.time.LocalDate;

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

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_Devolucao", nullable = false)
    private LocalDate dataDevolucao;

    public Emprestimo(EmprestimoRequestDTO emprestimoRequestDTO){
        this.pessoa = emprestimoRequestDTO.pessoa();
        this.livro = emprestimoRequestDTO.livro();
        this.dataEmprestimo = emprestimoRequestDTO.dataEmprestimo();
        this.dataDevolucao = emprestimoRequestDTO.dataDevolucao();
    }

    
}
