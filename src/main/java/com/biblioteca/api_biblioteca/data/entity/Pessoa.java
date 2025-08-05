package com.biblioteca.api_biblioteca.data.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;
import com.biblioteca.api_biblioteca.data.enums.PessoaRole;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pessoas")
public class Pessoa implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idPessoa;

    @Column(name="nome", nullable = false, length = 100)
    private String nome;

    @Column(name="cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name="cep", nullable = false, length = 9)
    private String cep;

    @Column(name="rua", length = 100)
    private String rua;

    @Column(name="bairro", length = 100)
    private String bairro;

    @Column(name="cidade", length = 100)
    private String cidade;

    @Column(name="estado", length = 2)
    private String estado;

    @Column(name="email", nullable = false, length = 100)
    private String email;

    @Column(name="senha", nullable = true, length = 100)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false, length = 50)
    private PessoaRole role;




    public Pessoa(PessoaRequestDTO pessoaRequestDTO){
        this.nome = pessoaRequestDTO.nome();
        this.cpf = pessoaRequestDTO.cpf();
        this.cep = pessoaRequestDTO.cep().replaceAll("[^\\d]", "");
        this.email = pessoaRequestDTO.email();
        this.role = pessoaRequestDTO.role();
    }

    public Pessoa(String nome, String cpf, String cep, String email, String senha){
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.email = email;
        this.senha = senha;
        this.role = PessoaRole.USER;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == PessoaRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
