package com.biblioteca.api_biblioteca.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.data.dto.request.EmprestimoRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.EmprestimoResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Emprestimo;
import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.data.entity.StatusEmprestimo;
import com.biblioteca.api_biblioteca.exceptions.general.BusinessException;
import com.biblioteca.api_biblioteca.exceptions.general.EntityNotFoundException;
import com.biblioteca.api_biblioteca.repository.EmprestimoRepository;
import com.biblioteca.api_biblioteca.repository.LivroRepository;

import jakarta.transaction.Transactional;

@Service
public class EmprestimoService {
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;



    @Transactional
    public EmprestimoResponseDTO criarEmprestimo(EmprestimoRequestDTO emprestimoRequestDTO, Pessoa pessoaLogada){

        Livro livro = livroRepository.findById(emprestimoRequestDTO.idLivro())
        // Exceção caso não encontre o livro
        .orElseThrow(() -> new EntityNotFoundException(emprestimoRequestDTO.idLivro()));

        // Exceção caso o livro não esteja disponível para empréstimo
        if(!livro.getDisponivel()){
            throw new BusinessException("O livro '" + livro.getNome() + "' não está disponível para empréstimo");
        }

        long diasDeEmprestimo = ChronoUnit.DAYS.between(LocalDate.now(), emprestimoRequestDTO.dataDevolucaoAgendada());
        if (diasDeEmprestimo > 180){
            throw new BusinessException("O período máximo de empréstimo é de 180 dias (6 meses)");
        }


        livro.setDisponivel(false);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setPessoa(pessoaLogada);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoAgendada(emprestimoRequestDTO.dataDevolucaoAgendada());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        return new EmprestimoResponseDTO(emprestimo);
    }

    @Transactional
    public EmprestimoResponseDTO realizarDevolucao(Long idEmprestimo, Pessoa pessoaLogada){
        Emprestimo emprestimo = getEmprestimoEntityById(idEmprestimo);

        if(!emprestimo.getPessoa().getIdPessoa().equals(pessoaLogada.getIdPessoa())){
            throw new BusinessException("Você não tem permissão para devolver um empréstimo que não é seu.");
        }

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new BusinessException("Esse empréstimo já foi finalizado.");
        }

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        return new EmprestimoResponseDTO(emprestimo);
    }

    // public EmprestimoResponseDTO atualizarEmprestimo(Long idEmprestimo, EmprestimoRequestDTO emprestimoRequestDTO){
        
    //     Emprestimo emprestimo = getEmprestimoEntityById(idEmprestimo);

    //     emprestimo.setPessoa(emprestimoRequestDTO.pessoa());
    //     emprestimo.setLivro(emprestimoRequestDTO.livro());
    //     emprestimo.setDataEmprestimo(emprestimoRequestDTO.dataEmprestimo());
    //     emprestimo.setDataDevolucaoAgendada(emprestimoRequestDTO.dataDevolucaoAgendada());
    //     emprestimo.setDataDevolucaoReal(emprestimoRequestDTO.dataDevolucaoReal());
    //     emprestimo.setStatus(emprestimoRequestDTO.status());

    //     emprestimoRepository.save(emprestimo);
    //     return new EmprestimoResponseDTO(emprestimo);
    // }

    // public String deletarEmprestimo(Long idEmprestimo){
        
    //     Emprestimo emprestimo = getEmprestimoEntityById(idEmprestimo);

    //     emprestimoRepository.delete(emprestimo);

    //     return "Emprestimo id: " + idEmprestimo + " deletado";
    // }

    public List<EmprestimoResponseDTO> getAllEmprestimos(){
        return emprestimoRepository.findAll().stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }
    
    public EmprestimoResponseDTO getEmprestimoById(Long idEmprestimo){
        return new EmprestimoResponseDTO(getEmprestimoEntityById(idEmprestimo));
    }

    private Emprestimo getEmprestimoEntityById(Long idEmprestimo){
        return emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new EntityNotFoundException(idEmprestimo));
    }
    
}
