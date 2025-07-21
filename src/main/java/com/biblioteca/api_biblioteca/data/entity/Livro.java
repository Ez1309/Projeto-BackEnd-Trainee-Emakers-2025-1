package com.biblioteca.api_biblioteca.data.entity;

import java.time.LocalDate;
import com.biblioteca.api_biblioteca.data.dto.request.LivroRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
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

    @Builder
    public Livro(LivroRequestDTO livroRequestDTO){
        this.nome = livroRequestDTO.nome();
        this.autor = livroRequestDTO.autor();
        this.dataLancamento = livroRequestDTO.dataLancamento();
    }

}
