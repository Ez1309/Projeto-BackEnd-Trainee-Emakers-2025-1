package com.biblioteca.api_biblioteca.data.entity;

public enum PessoaRole {
    
    ADMIN("admin"),
    PESSOA("pessoa");

    private String role;

    PessoaRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
