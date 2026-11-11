package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.AutorRequestDTO;
import br.edu.christus.backend.domain.dto.AutorResponseDTO;
import br.edu.christus.backend.service.AutorService;
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
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "CRUD dos autores dos livros e materiais digitais")
public class AutorController {

    @Autowired
    private AutorService service;

    @Operation(summary = "Cadastra um novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Autor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome em branco, data no futuro)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutorResponseDTO create(@RequestBody @Valid AutorRequestDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Lista os autores (filtro opcional por parte do nome)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<AutorResponseDTO> findAll(
            @Parameter(description = "Parte do nome do autor, ex: machado")
            @RequestParam(name = "nome", required = false) String nome) {
        return service.findAll(nome);
    }

    @Operation(summary = "Consulta um autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    @GetMapping("/{id}")
    public AutorResponseDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza os dados de um autor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    @PutMapping("/{id}")
    public AutorResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid AutorRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Exclui um autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "409", description = "Autor possui livros cadastrados")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
