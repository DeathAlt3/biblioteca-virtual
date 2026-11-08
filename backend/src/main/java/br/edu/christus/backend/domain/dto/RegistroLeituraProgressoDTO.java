package br.edu.christus.backend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroLeituraProgressoDTO {

    @NotNull(message = "O número total de páginas lidas até o momento é obrigatório")
    @Min(value = 1, message = "O número de páginas lidas deve ser pelo menos 1")
    private Integer paginasLidas;

    @NotNull(message = "Os minutos estudados nesta sessão são obrigatórios")
    @Min(value = 1, message = "O tempo de estudo da sessão deve ser de pelo menos 1 minuto")
    private Integer minutosSessao;
}
