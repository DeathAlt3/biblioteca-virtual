package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.DisciplinaRequestDTO;
import br.edu.christus.backend.domain.dto.DisciplinaResponseDTO;
import br.edu.christus.backend.service.DisciplinaService;
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
@RequestMapping("/api/v1/disciplinas")
@Tag(name = "Disciplinas", description = "CRUD das disciplinas usadas para organizar o acervo")
public class DisciplinaController {

    @Autowired
    private DisciplinaService service;

    @Operation(summary = "Cadastra uma nova disciplina")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disciplina cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: série fora de 1 a 3)"),
            @ApiResponse(responseCode = "409", description = "Já existe disciplina com esse nome")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplinaResponseDTO create(@RequestBody @Valid DisciplinaRequestDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Lista as disciplinas (filtro opcional por série)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<DisciplinaResponseDTO> findAll(
            @Parameter(description = "Série do ensino médio: 1, 2 ou 3")
            @RequestParam(name = "serie", required = false) Integer serie) {
        return service.findAll(serie);
    }

    @Operation(summary = "Consulta uma disciplina pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    @GetMapping("/{id}")
    public DisciplinaResponseDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza uma disciplina")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
            @ApiResponse(responseCode = "409", description = "Já existe outra disciplina com esse nome")
    })
    @PutMapping("/{id}")
    public DisciplinaResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid DisciplinaRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Exclui uma disciplina pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disciplina excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
            @ApiResponse(responseCode = "409", description = "Disciplina possui livros vinculados")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
