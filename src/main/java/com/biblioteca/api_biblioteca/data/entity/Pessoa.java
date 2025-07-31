package com.biblioteca.api_biblioteca.data.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pessoas")
public class Pessoa implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false, length = 50)
    private PessoaRole role;




    public Pessoa(PessoaRequestDTO pessoaRequestDTO){
        this.nome = pessoaRequestDTO.nome();
        this.cpf = pessoaRequestDTO.cpf();
        this.cep = pessoaRequestDTO.cep().replaceAll("[^\\d]", "");
        this.email = pessoaRequestDTO.email();
        this.senha = pessoaRequestDTO.senha();
        this.role = pessoaRequestDTO.role();
    }

    public Pessoa(String nome, String cpf, String cep, String email, String senha, PessoaRole role){
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == PessoaRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_PESSOA"));
        else return List.of(new SimpleGrantedAuthority("ROLE_PESSOA"));
    }



    @Override
    public String getPassword() {
        return senha;
    }



    @Override
    public String getUsername() {
        return email;
    }

}
