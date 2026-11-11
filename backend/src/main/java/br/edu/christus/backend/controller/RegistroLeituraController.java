package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.RegistroLeituraProgressoDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraRequestDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraResponseDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraUpdateDTO;
import br.edu.christus.backend.service.RegistroLeituraService;
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
@RequestMapping("/api/v1/leituras")
@Tag(name = "Registros de Leitura Digital", description = "Acompanhamento do progresso de leitura e estudo digital dos estudantes")
public class RegistroLeituraController {

    @Autowired
    private RegistroLeituraService service;

    @Operation(summary = "Inicia uma nova sessão de leitura digital de um material")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sessão de leitura iniciada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "404", description = "Estudante ou Livro não encontrados"),
            @ApiResponse(responseCode = "409", description = "O estudante já iniciou a leitura deste material")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroLeituraResponseDTO iniciar(@RequestBody @Valid RegistroLeituraRequestDTO dto) {
        return service.iniciarLeitura(dto);
    }

    @Operation(summary = "[FUNCIONALIDADE EXTRA] Atualiza o progresso de leitura digital com cálculo automático de conclusão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progresso atualizado e métricas recalculadas"),
            @ApiResponse(responseCode = "400", description = "Páginas lidas inválidas ou acima do limite do livro"),
            @ApiResponse(responseCode = "404", description = "Registro de leitura não encontrado")
    })
    @PatchMapping("/{id}/progresso")
    public RegistroLeituraResponseDTO atualizarProgresso(
            @PathVariable Long id, 
            @RequestBody @Valid RegistroLeituraProgressoDTO dto) {
        return service.atualizarProgresso(id, dto);
    }

    @Operation(summary = "Edita um registro de leitura (status EM_ANDAMENTO/PAUSADO e dispositivo)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status ou dispositivo inválidos"),
            @ApiResponse(responseCode = "404", description = "Registro de leitura não encontrado"),
            @ApiResponse(responseCode = "409", description = "Leitura já concluída")
    })
    @PutMapping("/{id}")
    public RegistroLeituraResponseDTO update(
            @PathVariable("id") Long id,
            @RequestBody @Valid RegistroLeituraUpdateDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Lista todos os registros de leitura (com filtros por estudante ou livro)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public List<RegistroLeituraResponseDTO> findAll(
            @Parameter(description = "Filtrar por ID do estudante")
            @RequestParam(required = false) Long usuarioId,
            @Parameter(description = "Filtrar por ID do livro digital")
            @RequestParam(required = false) Long livroId) {
        return service.findAll(usuarioId, livroId);
    }

    @Operation(summary = "Consulta um registro de leitura pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    @GetMapping("/{id}")
    public RegistroLeituraResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Exclui um registro de leitura pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
