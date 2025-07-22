// package com.biblioteca.api_biblioteca.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.biblioteca.api_biblioteca.data.dto.request.EmprestimoRequestDTO;
// import com.biblioteca.api_biblioteca.data.dto.response.EmprestimoResponseDTO;
// import com.biblioteca.api_biblioteca.data.entity.Emprestimo;
// import com.biblioteca.api_biblioteca.repository.EmprestimoRepository;

// @Service
// public class EmprestimoService {
    
//     @Autowired
//     private EmprestimoRepository emprestimoRepository;

//     public List<EmprestimoResponseDTO> getAllEmprestimos(){
//         List<Emprestimo> emprestimos = emprestimoRepository.findAll();

//         return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
//     }
    
//     public EmprestimoResponseDTO getEmprestimoById(Long idEmprestimo){
//         Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));

//         return new EmprestimoResponseDTO(emprestimo);
//     }

//     public EmprestimoResponseDTO criarEmprestimo(EmprestimoRequestDTO emprestimoRequestDTO){
//         Emprestimo emprestimo = new Emprestimo(emprestimoRequestDTO);
//         emprestimoRepository.save(emprestimo);

//         return new EmprestimoResponseDTO(emprestimo);
//     }

//     public EmprestimoResponseDTO atualizarEmprestimo(Long idEmprestimo, EmprestimoRequestDTO emprestimoRequestDTO){
        
//         Emprestimo emprestimo = getEmprestimoEntityById(idEmprestimo);

//         emprestimo.setNome(emprestimoRequestDTO.nome());
//         emprestimo.setAutor(emprestimoRequestDTO.autor());
//         emprestimo.setDataLancamento(emprestimoRequestDTO.dataLancamento());

//         emprestimoRepository.save(emprestimo);

//         return new EmprestimoResponseDTO(emprestimo);
//     }

//     public String deletarEmprestimo(Long idEmprestimo){
        
//         Emprestimo emprestimo = getEmprestimoEntityById(idEmprestimo);

//         emprestimoRepository.delete(emprestimo);

//         return "Emprestimo id: " + idEmprestimo + " deletado";
//     }

//     private Emprestimo getEmprestimoEntityById(Long idEmprestimo){
//         return emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));
//     }
    
// }
