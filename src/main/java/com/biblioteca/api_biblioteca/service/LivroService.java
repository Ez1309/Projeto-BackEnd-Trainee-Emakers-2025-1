package com.biblioteca.api_biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.data.dto.request.LivroRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LivroResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.biblioteca.api_biblioteca.repository.LivroRepository;

@Service
public class LivroService {
    
    @Autowired
    private LivroRepository livroRepository;

    public List<LivroResponseDTO> getAllLivros(){
        List<Livro> livros = livroRepository.findAll();

        return livros.stream().map(LivroResponseDTO::new).collect(Collectors.toList());
    }
    
    public LivroResponseDTO getLivroById(Long idLivro){
        Livro livro = livroRepository.findById(idLivro).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO criarLivro(LivroRequestDTO livroRequestDTO){
        Livro livro = new Livro(livroRequestDTO);
        livroRepository.save(livro);

        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO atualizarLivro(Long idLivro, LivroRequestDTO livroRequestDTO){
        
        Livro livro = getLivroEntityById(idLivro);

        livro.setNome(livroRequestDTO.nome());
        livro.setAutor(livroRequestDTO.autor());
        livro.setDataLancamento(livroRequestDTO.dataLancamento());

        livroRepository.save(livro);

        return new LivroResponseDTO(livro);
    }

    public String deletarLivro(Long idLivro, LivroRequestDTO livroRequestDTO){
        
        Livro livro = getLivroEntityById(idLivro);

        livroRepository.delete(livro);

        return "Livro id: " + idLivro + " deletado";
    }

    private Livro getLivroEntityById(Long idLivro){
        return livroRepository.findById(idLivro).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }
    
}
