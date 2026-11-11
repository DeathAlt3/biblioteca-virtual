package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.LivroResponseDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraProgressoDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraRequestDTO;
import br.edu.christus.backend.domain.dto.RegistroLeituraResponseDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.domain.model.RegistroLeitura;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.repository.RegistroLeituraRepository;
import br.edu.christus.backend.repository.UserRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroLeituraService {

    @Autowired
    private RegistroLeituraRepository registroRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LivroRepository livroRepository;

    public RegistroLeituraResponseDTO iniciarLeitura(RegistroLeituraRequestDTO dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estudante não encontrado com o ID: " + dto.getUsuarioId()));

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Material digital não encontrado com o ID: " + dto.getLivroId()));

        if (registroRepository.existsByUsuarioIdAndLivroId(usuario.getId(), livro.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O estudante já iniciou a leitura deste material. Utilize a atualização de progresso.");
        }

        // Incrementa o contador de acessos do material digital
        livro.setTotalAcessos(livro.getTotalAcessos() + 1);
        livroRepository.save(livro);

        RegistroLeitura registro = new RegistroLeitura();
        registro.setUsuario(usuario);
        registro.setLivro(livro);
        registro.setDataInicio(LocalDate.now());
        registro.setDataUltimoAcesso(LocalDate.now());
        registro.setPaginasLidas(0);
        registro.setPercentualConcluido(0.0);
        registro.setTempoEstudoMinutos(0);
        registro.setStatus("EM_ANDAMENTO");
        registro.setDispositivo(dto.getDispositivo() != null ? dto.getDispositivo().toUpperCase() : "COMPUTADOR");

        RegistroLeitura salvo = registroRepository.save(registro);
        return toResponseDTO(salvo);
    }

    public RegistroLeituraResponseDTO atualizarProgresso(Long id, RegistroLeituraProgressoDTO dto) {
        RegistroLeitura registro = registroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Registro de leitura não encontrado com o ID: " + id));

        Livro livro = registro.getLivro();
        if (dto.getPaginasLidas() > livro.getNumeroPaginas()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O número de páginas lidas (" + dto.getPaginasLidas() + 
                    ") não pode ser maior que o total do livro (" + livro.getNumeroPaginas() + ")");
        }

        registro.setPaginasLidas(dto.getPaginasLidas());
        registro.setTempoEstudoMinutos(registro.getTempoEstudoMinutos() + dto.getMinutosSessao());
        registro.setDataUltimoAcesso(LocalDate.now());

        // Cálculo automático de percentual concluído
        double percentual = ((double) dto.getPaginasLidas() / livro.getNumeroPaginas()) * 100.0;
        registro.setPercentualConcluido(Math.round(percentual * 10.0) / 10.0);

        if (registro.getPercentualConcluido() >= 100.0) {
            registro.setStatus("CONCLUIDO");
        } else {
            registro.setStatus("EM_ANDAMENTO");
        }

        RegistroLeitura atualizado = registroRepository.save(registro);
        return toResponseDTO(atualizado);
    }

    public List<RegistroLeituraResponseDTO> findAll(Long usuarioId, Long livroId) {
        List<RegistroLeitura> lista;
        if (usuarioId != null) {
            lista = registroRepository.findByUsuarioId(usuarioId);
        } else if (livroId != null) {
            lista = registroRepository.findByLivroId(livroId);
        } else {
            lista = registroRepository.findAll();
        }
        return lista.stream().map(this::toResponseDTO).toList();
    }

    public RegistroLeituraResponseDTO findById(Long id) {
        RegistroLeitura registro = registroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Registro de leitura não encontrado com o ID: " + id));
        return toResponseDTO(registro);
    }

    public void delete(Long id) {
        if (!registroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Registro de leitura não encontrado com o ID: " + id);
        }
        registroRepository.deleteById(id);
    }

    private RegistroLeituraResponseDTO toResponseDTO(RegistroLeitura reg) {
        RegistroLeituraResponseDTO dto = new RegistroLeituraResponseDTO();
        dto.setId(reg.getId());
        dto.setDataInicio(reg.getDataInicio());
        dto.setDataUltimoAcesso(reg.getDataUltimoAcesso());
        dto.setPaginasLidas(reg.getPaginasLidas());
        dto.setPercentualConcluido(reg.getPercentualConcluido());
        dto.setTempoEstudoMinutos(reg.getTempoEstudoMinutos());
        dto.setStatus(reg.getStatus());
        dto.setDispositivo(reg.getDispositivo());

        // Estimativa inteligente de conclusão baseada no ritmo do estudante
        Livro livro = reg.getLivro();
        if (livro != null && reg.getPaginasLidas() > 0 && reg.getPercentualConcluido() < 100.0) {
            int paginasRestantes = livro.getNumeroPaginas() - reg.getPaginasLidas();
            double ritmoPorDia = (double) reg.getPaginasLidas() / 
                    Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(reg.getDataInicio(), LocalDate.now()) + 1);
            dto.setEstimativaDiasParaConcluir((int) Math.ceil(paginasRestantes / Math.max(1.0, ritmoPorDia)));
        } else {
            dto.setEstimativaDiasParaConcluir(0);
        }

        if (reg.getUsuario() != null) {
            dto.setUsuario(MapperUtil.parseObject(reg.getUsuario(), UserLowDTO.class));
        }
        if (reg.getLivro() != null) {
            dto.setLivro(LivroResponseDTO.fromEntity(reg.getLivro()));
        }
        return dto;
    }
}
