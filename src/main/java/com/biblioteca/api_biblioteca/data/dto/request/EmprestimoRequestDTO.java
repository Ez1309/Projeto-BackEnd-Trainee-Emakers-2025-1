package com.biblioteca.api_biblioteca.data.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record EmprestimoRequestDTO(

    @NotNull(message = "O ID do livro é obrigatório")
    Long idLivro,

    @NotNull(message = "A data de devolução agendada é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Future(message = "A data de devolução deve ser uma data futura.")
    LocalDate dataDevolucaoAgendada

) {
}
