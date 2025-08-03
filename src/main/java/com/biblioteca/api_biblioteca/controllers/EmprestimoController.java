package com.biblioteca.api_biblioteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.api_biblioteca.data.dto.request.EmprestimoRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.EmprestimoResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<EmprestimoResponseDTO>> getAllEmprestimos(){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getAllEmprestimos());
    }

    @GetMapping(value = "/meus-emprestimos")
    public ResponseEntity<List<EmprestimoResponseDTO>> getMeusEmprestimos(@AuthenticationPrincipal Pessoa pessoaLogada){
        List<EmprestimoResponseDTO> emprestimos = emprestimoService.getEmprestimos(pessoaLogada);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping(value = "/{idEmprestimo}/find")
    public ResponseEntity<EmprestimoResponseDTO> getEmprestimoById(@PathVariable Long idEmprestimo){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getEmprestimoById(idEmprestimo));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<EmprestimoResponseDTO> criarEmprestimo(@RequestBody @Valid EmprestimoRequestDTO emprestimoRequestDTO, @AuthenticationPrincipal Pessoa pessoaLogada){
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criarEmprestimo(emprestimoRequestDTO, pessoaLogada));
    }

    @PatchMapping(value = "/{idEmprestimo}/devolucao")
    public ResponseEntity<EmprestimoResponseDTO> realizarDevolucao(@PathVariable Long idEmprestimo, @AuthenticationPrincipal Pessoa pessoaLogada){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.realizarDevolucao(idEmprestimo, pessoaLogada));
    }

    // @PutMapping (value = "/update/{idEmprestimo}")
    // public ResponseEntity<EmprestimoResponseDTO> atualizarEmprestimo(@PathVariable Long idEmprestimo, @RequestBody @Valid EmprestimoRequestDTO emprestimoRequestDTO){
    //     return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.atualizarEmprestimo(idEmprestimo, emprestimoRequestDTO));
    // }

    // @DeleteMapping(value = "/delete/{idEmprestimo}")
    // public ResponseEntity<String> deletarEmprestimo(@PathVariable Long idEmprestimo){
    //     return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.deletarEmprestimo(idEmprestimo));
    // }
}
