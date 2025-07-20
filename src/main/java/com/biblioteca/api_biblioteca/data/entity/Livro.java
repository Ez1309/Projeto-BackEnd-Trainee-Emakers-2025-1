package com.biblioteca.api_biblioteca.data.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idLivro;

    @Column(name="nome", nullable = false, length = 100)
    private String nome;

    @Column(name="autor", nullable = false, length = 100)
    private String autor;

    @Column(name="data_lancamento", nullable = false)
    private LocalDate dataLancamento;

}
