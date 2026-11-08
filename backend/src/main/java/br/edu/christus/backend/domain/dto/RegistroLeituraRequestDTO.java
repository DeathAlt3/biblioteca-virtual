package br.edu.christus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroLeituraRequestDTO {

    @NotNull(message = "O ID do estudante é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID do material digital é obrigatório")
    private Long livroId;

    @Size(max = 30, message = "O nome do dispositivo deve ter no máximo 30 caracteres (ex: SMARTPHONE, COMPUTADOR)")
    private String dispositivo;
}
