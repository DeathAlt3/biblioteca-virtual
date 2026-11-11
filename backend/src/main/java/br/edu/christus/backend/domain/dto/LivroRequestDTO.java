package br.edu.christus.backend.domain.dto;

import br.edu.christus.backend.domain.model.FormatoDigital;
import br.edu.christus.backend.domain.model.TipoMaterial;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    private String titulo;

    @Pattern(regexp = "^[0-9Xx-]{10,17}$",
            message = "ISBN inválido: use de 10 a 17 caracteres com números e hífens (ex: 978-85-359-0277-1)")
    private String isbn;

    @NotNull(message = "O ano de publicação é obrigatório")
    @Min(value = 1450, message = "O ano de publicação deve ser maior ou igual a 1450")
    private Integer anoPublicacao;

    @NotNull(message = "O número de páginas é obrigatório")
    @Positive(message = "O número de páginas deve ser maior que zero")
    private Integer numeroPaginas;

    @NotNull(message = "O tipo do material é obrigatório (LIVRO, APOSTILA, ARTIGO ou RESUMO)")
    private TipoMaterial tipo;

    @NotNull(message = "O formato é obrigatório (PDF, EPUB ou HTML)")
    private FormatoDigital formato;

    @NotBlank(message = "A URL de acesso ao material digital é obrigatória")
    @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres")
    @Pattern(regexp = "^https?://.+", message = "A URL de acesso deve começar com http:// ou https://")
    private String urlAcesso;

    @Size(max = 2000, message = "A sinopse deve ter no máximo 2000 caracteres")
    private String sinopse;

    @NotNull(message = "O ID do autor é obrigatório")
    private Long autorId;

    @NotNull(message = "O ID da disciplina é obrigatório")
    private Long disciplinaId;
}
