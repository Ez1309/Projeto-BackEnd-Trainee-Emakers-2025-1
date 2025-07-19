package com.ezequiel.api_biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ezequiel.api_biblioteca.repositories.LivroRepository;

@RestController
public class LivroController {
    
    @Autowired
    LivroRepository livroRepository;
}
