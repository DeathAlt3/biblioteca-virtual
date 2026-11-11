package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.LivroRequestDTO;
import br.edu.christus.backend.domain.dto.LivroResponseDTO;
import br.edu.christus.backend.service.LivroService;
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
@RequestMapping("/api/v1/livros")
@Tag(name = "Livros e Materiais Digitais", description = "CRUD do acervo digital (livros, apostilas, artigos e resumos)")
public class LivroController {

    @Autowired
    private LivroService service;

    @Operation(summary = "Cadastra um novo livro/material digital")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: páginas <= 0, URL inválida, ano no futuro)"),
            @ApiResponse(responseCode = "404", description = "Autor ou Disciplina não encontrados"),
            @ApiResponse(responseCode = "409", description = "ISBN já cadastrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponseDTO create(@RequestBody @Valid LivroRequestDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Lista o acervo (filtros opcionais: título, autor ou disciplina)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<LivroResponseDTO> findAll(
            @Parameter(description = "Parte do título, ex: casmurro")
            @RequestParam(name = "titulo", required = false) String titulo,
            @Parameter(description = "ID do autor")
            @RequestParam(name = "autorId", required = false) Long autorId,
            @Parameter(description = "ID da disciplina")
            @RequestParam(name = "disciplinaId", required = false) Long disciplinaId) {
        return service.findAll(titulo, autorId, disciplinaId);
    }

    @Operation(summary = "Ranking dos 5 materiais mais acessados")
    @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso")
    @GetMapping("/mais-acessados")
    public List<LivroResponseDTO> maisAcessados() {
        return service.maisAcessados();
    }

    @Operation(summary = "Consulta um livro/material pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material encontrado"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    @GetMapping("/{id}")
    public LivroResponseDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza um livro/material")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Material, Autor ou Disciplina não encontrados"),
            @ApiResponse(responseCode = "409", description = "ISBN já usado por outro material")
    })
    @PutMapping("/{id}")
    public LivroResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid LivroRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Registra um acesso ao material (soma 1 ao contador de acessos)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acesso registrado; retorna o material com a URL de acesso"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    @PatchMapping("/{id}/acesso")
    public LivroResponseDTO registrarAcesso(@PathVariable("id") Long id) {
        return service.registrarAcesso(id);
    }

    @Operation(summary = "Exclui um livro/material pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Material excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado"),
            @ApiResponse(responseCode = "409", description = "Material possui avaliações ou registros de leitura")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
