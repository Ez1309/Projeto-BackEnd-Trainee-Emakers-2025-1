package com.biblioteca.api_biblioteca.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.data.dto.request.AtualizarEstoqueRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.LivroRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LivroResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.biblioteca.api_biblioteca.exceptions.emprestimo.LivroIndisponivelException;
import com.biblioteca.api_biblioteca.exceptions.general.EntidadeNaoEncontradaException;
import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;
import com.biblioteca.api_biblioteca.exceptions.livro.LivroDuplicadoException;
import com.biblioteca.api_biblioteca.repository.LivroRepository;

import jakarta.transaction.Transactional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<LivroResponseDTO> getAllLivros() {
        List<Livro> livros = livroRepository.findAll();

        return livros.stream().map(LivroResponseDTO::new).collect(Collectors.toList());
    }

    public LivroResponseDTO getLivroById(Long idLivro) {
        Livro livro = getLivroEntityById(idLivro);

        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO criarLivro(LivroRequestDTO livroRequestDTO) {

        if (livroRepository.existsByNomeAndAutorAndDataLancamento(livroRequestDTO.nome(), livroRequestDTO.autor(),
                livroRequestDTO.dataLancamento())) {
            throw new LivroDuplicadoException("Um livro com essas características já existe.");
        }

        Livro livro = new Livro(livroRequestDTO);
        livroRepository.save(livro);

        return new LivroResponseDTO(livro);
    }

    @Transactional
    public LivroResponseDTO atualizarEstoque(Long idLivro, AtualizarEstoqueRequestDTO atualizarEstoqueRequestDTO){
        Livro livro = getLivroEntityById(idLivro);

        int quantidadeAjuste = atualizarEstoqueRequestDTO.quantidade();

        if ((livro.getQuantidadeTotal() + quantidadeAjuste) < 0){
            throw new OperacaoInvalidaException("Ajuste inválido. O estoque total não pode ser negativo");
        }

        if ((livro.getQuantidadeDisponivel() + quantidadeAjuste) > (livro.getQuantidadeTotal() + quantidadeAjuste)) {
            throw new OperacaoInvalidaException("Ajuste inválido. A quantidade disponível não pode exceder a quantidade total.");
        }

        livro.setQuantidadeTotal(livro.getQuantidadeTotal() + quantidadeAjuste);
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + quantidadeAjuste);

        livroRepository.save(livro);
        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO atualizarLivro(Long idLivro, LivroRequestDTO livroRequestDTO) {

        Livro livro = getLivroEntityById(idLivro);

        Optional<Livro> livroExistente = livroRepository.findByNomeAndAutorAndDataLancamento(livroRequestDTO.nome(),
                livroRequestDTO.autor(), livroRequestDTO.dataLancamento());

        if (livroExistente.isPresent() && !livroExistente.get().getIdLivro().equals(idLivro)) {
            throw new LivroDuplicadoException("Um livro com essas características já existe");
        }

        livro.setNome(livroRequestDTO.nome());
        livro.setAutor(livroRequestDTO.autor());
        livro.setDataLancamento(livroRequestDTO.dataLancamento());

        livroRepository.save(livro);

        return new LivroResponseDTO(livro);
    }

    public void deletarLivro(Long idLivro) {

        Livro livro = getLivroEntityById(idLivro);

        if (livro.getQuantidadeDisponivel() < livro.getQuantidadeTotal()) {
            throw new LivroIndisponivelException("Não é possível deletar um livro que está atualmente emprestado.");
        }

        livroRepository.delete(livro);

    }

    private Livro getLivroEntityById(Long idLivro) {
        return livroRepository.findById(idLivro).orElseThrow(() -> new EntidadeNaoEncontradaException(idLivro));
    }

}
