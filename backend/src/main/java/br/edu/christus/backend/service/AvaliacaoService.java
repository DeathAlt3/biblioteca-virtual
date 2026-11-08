package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.AvaliacaoRequestDTO;
import br.edu.christus.backend.domain.dto.AvaliacaoResponseDTO;
import br.edu.christus.backend.domain.dto.LivroResponseDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.Avaliacao;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.repository.AvaliacaoRepository;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.repository.UserRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LivroRepository livroRepository;

    public AvaliacaoResponseDTO create(AvaliacaoRequestDTO dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com o ID: " + dto.getUsuarioId()));

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Livro não encontrado com o ID: " + dto.getLivroId()));

        if (avaliacaoRepository.existsByUsuarioIdAndLivroId(dto.getUsuarioId(), dto.getLivroId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Este estudante já avaliou este livro anteriormente. Utilize a edição caso queira alterar.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setUsuario(usuario);
        avaliacao.setLivro(livro);
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setRecomendariaColegas(dto.getRecomendariaColegas());
        avaliacao.setDataAvaliacao(LocalDate.now());

        Avaliacao salvo = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(salvo);
    }

    public AvaliacaoResponseDTO update(Long id, AvaliacaoRequestDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Avaliação não encontrada com o ID: " + id));

        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setRecomendariaColegas(dto.getRecomendariaColegas());

        Avaliacao atualizado = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(atualizado);
    }

    public List<AvaliacaoResponseDTO> findAll(Long livroId, Long usuarioId) {
        List<Avaliacao> lista;
        if (livroId != null) {
            lista = avaliacaoRepository.findByLivroId(livroId);
        } else if (usuarioId != null) {
            lista = avaliacaoRepository.findByUsuarioId(usuarioId);
        } else {
            lista = avaliacaoRepository.findAll();
        }
        return lista.stream().map(this::toResponseDTO).toList();
    }

    public AvaliacaoResponseDTO findById(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Avaliação não encontrada com o ID: " + id));
        return toResponseDTO(avaliacao);
    }

    public void delete(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Avaliação não encontrada com o ID: " + id);
        }
        avaliacaoRepository.deleteById(id);
    }

    private AvaliacaoResponseDTO toResponseDTO(Avaliacao avaliacao) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setId(avaliacao.getId());
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());
        dto.setRecomendariaColegas(avaliacao.getRecomendariaColegas());

        if (avaliacao.getUsuario() != null) {
            dto.setUsuario(MapperUtil.parseObject(avaliacao.getUsuario(), UserLowDTO.class));
        }
        if (avaliacao.getLivro() != null) {
            dto.setLivro(MapperUtil.parseObject(avaliacao.getLivro(), LivroResponseDTO.class));
        }
        return dto;
    }
}
