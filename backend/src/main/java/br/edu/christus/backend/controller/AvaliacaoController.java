package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.AvaliacaoRequestDTO;
import br.edu.christus.backend.domain.dto.AvaliacaoResponseDTO;
import br.edu.christus.backend.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avaliacoes")
@Tag(name = "Avaliações", description = "Endpoints para resenhas e recomendações de livros entre estudantes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService service;

    @Operation(summary = "Cadastra uma nova resenha/avaliação de livro feita por um estudante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da avaliação inválidos (ex: nota fora de 1 a 5)"),
            @ApiResponse(responseCode = "404", description = "Estudante ou Livro não encontrados"),
            @ApiResponse(responseCode = "409", description = "Estudante já avaliou este livro anteriormente")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponseDTO create(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Atualiza uma avaliação existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @PutMapping("/{id}")
    public AvaliacaoResponseDTO update(@PathVariable Long id, @RequestBody @Valid AvaliacaoRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Lista avaliações (com filtros opcionais por livro ou estudante)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<AvaliacaoResponseDTO> findAll(
            @Parameter(description = "Filtrar resenhas pelo ID do livro")
            @RequestParam(required = false) Long livroId,
            @Parameter(description = "Filtrar resenhas pelo ID do estudante")
            @RequestParam(required = false) Long usuarioId) {
        return service.findAll(livroId, usuarioId);
    }

    @Operation(summary = "Consulta uma avaliação pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @GetMapping("/{id}")
    public AvaliacaoResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Exclui uma avaliação pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avaliação excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
