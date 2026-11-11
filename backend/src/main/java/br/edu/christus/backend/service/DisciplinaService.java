package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.DisciplinaRequestDTO;
import br.edu.christus.backend.domain.dto.DisciplinaResponseDTO;
import br.edu.christus.backend.domain.model.Disciplina;
import br.edu.christus.backend.repository.DisciplinaRepository;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private LivroRepository livroRepository;

    public DisciplinaResponseDTO create(DisciplinaRequestDTO dto) {
        if (disciplinaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma disciplina cadastrada com o nome: " + dto.getNome());
        }
        Disciplina disciplina = MapperUtil.parseObject(dto, Disciplina.class);
        return MapperUtil.parseObject(disciplinaRepository.save(disciplina), DisciplinaResponseDTO.class);
    }

    public List<DisciplinaResponseDTO> findAll(Integer serie) {
        List<Disciplina> lista = (serie != null)
                ? disciplinaRepository.findBySerie(serie)
                : disciplinaRepository.findAll();
        return MapperUtil.parseListObjects(lista, DisciplinaResponseDTO.class);
    }

    public DisciplinaResponseDTO findById(Long id) {
        return MapperUtil.parseObject(buscarDisciplina(id), DisciplinaResponseDTO.class);
    }

    public DisciplinaResponseDTO update(Long id, DisciplinaRequestDTO dto) {
        Disciplina disciplina = buscarDisciplina(id);
        if (disciplinaRepository.existsByNomeIgnoreCaseAndIdNot(dto.getNome(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe outra disciplina cadastrada com o nome: " + dto.getNome());
        }
        disciplina.setNome(dto.getNome());
        disciplina.setAreaConhecimento(dto.getAreaConhecimento());
        disciplina.setSerie(dto.getSerie());
        disciplina.setDescricao(dto.getDescricao());
        return MapperUtil.parseObject(disciplinaRepository.save(disciplina), DisciplinaResponseDTO.class);
    }

    public void delete(Long id) {
        buscarDisciplina(id);
        if (livroRepository.existsByDisciplinaId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir a disciplina de ID " + id
                            + " porque existem livros vinculados a ela.");
        }
        disciplinaRepository.deleteById(id);
    }

    /** Busca a entidade ou lança 404. Também é usado pelo LivroService. */
    public Disciplina buscarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Disciplina não encontrada com o ID: " + id));
    }
}
