package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.AutorRequestDTO;
import br.edu.christus.backend.domain.dto.AutorResponseDTO;
import br.edu.christus.backend.domain.model.Autor;
import br.edu.christus.backend.repository.AutorRepository;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    public AutorResponseDTO create(AutorRequestDTO dto) {
        // DTO -> Entidade (ModelMapper copia os campos de mesmo nome)
        Autor autor = MapperUtil.parseObject(dto, Autor.class);
        Autor salvo = autorRepository.save(autor);
        // Entidade -> DTO de resposta
        return MapperUtil.parseObject(salvo, AutorResponseDTO.class);
    }

    public List<AutorResponseDTO> findAll(String nome) {
        List<Autor> autores = (nome != null && !nome.isBlank())
                ? autorRepository.findByNomeContainingIgnoreCase(nome)
                : autorRepository.findAll();
        return MapperUtil.parseListObjects(autores, AutorResponseDTO.class);
    }

    public AutorResponseDTO findById(Long id) {
        return MapperUtil.parseObject(buscarAutor(id), AutorResponseDTO.class);
    }

    public AutorResponseDTO update(Long id, AutorRequestDTO dto) {
        Autor autor = buscarAutor(id);
        autor.setNome(dto.getNome());
        autor.setNacionalidade(dto.getNacionalidade());
        autor.setDataNascimento(dto.getDataNascimento());
        autor.setBiografia(dto.getBiografia());
        return MapperUtil.parseObject(autorRepository.save(autor), AutorResponseDTO.class);
    }

    public void delete(Long id) {
        buscarAutor(id);
        // Regra de negócio: não apagar autor que ainda possui livros no acervo
        if (livroRepository.existsByAutorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir o autor de ID " + id
                            + " porque ele possui livros cadastrados. Exclua ou altere esses livros antes.");
        }
        autorRepository.deleteById(id);
    }

    /** Busca a entidade ou lança 404. Também é usado pelo LivroService. */
    public Autor buscarAutor(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + id));
    }
}
