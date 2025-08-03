package com.biblioteca.api_biblioteca.data.enums;

public enum PessoaRole {
    
    ADMIN("admin"),
    USER("user");

    private String role;

    PessoaRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
