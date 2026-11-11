package br.edu.christus.backend.config;

import br.edu.christus.backend.domain.model.*;
import br.edu.christus.backend.repository.AutorRepository;
import br.edu.christus.backend.repository.DisciplinaRepository;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Cadastra dados de exemplo quando a aplicação sobe com o banco vazio,
 * para que as rotas possam ser demonstradas imediatamente no Swagger.
 * Se já houver dados (ex: PostgreSQL), nada é inserido.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(novoUsuario("Ana Clara Souza", "ana.souza@escola.edu.br"));
            userRepository.save(novoUsuario("Pedro Henrique Lima", "pedro.lima@escola.edu.br"));
        }

        if (livroRepository.count() > 0) {
            return;
        }

        Autor machado = autorRepository.save(new Autor(null, "Machado de Assis", "Brasileira",
                LocalDate.of(1839, 6, 21), "Fundador da Academia Brasileira de Letras e um dos maiores nomes do Realismo."));
        Autor aluisio = autorRepository.save(new Autor(null, "Aluísio Azevedo", "Brasileira",
                LocalDate.of(1857, 4, 14), "Principal representante do Naturalismo no Brasil."));
        Autor equipe = autorRepository.save(new Autor(null, "Equipe Pedagógica da Escola", "Brasileira",
                null, "Professores que produzem as apostilas e resumos da escola."));

        Disciplina literatura = disciplinaRepository.save(new Disciplina(null, "Literatura",
                AreaConhecimento.LINGUAGENS, 2, "Literatura brasileira: Romantismo, Realismo e Naturalismo."));
        Disciplina matematica = disciplinaRepository.save(new Disciplina(null, "Matemática",
                AreaConhecimento.MATEMATICA, 1, "Conjuntos, funções e progressões."));
        Disciplina biologia = disciplinaRepository.save(new Disciplina(null, "Biologia",
                AreaConhecimento.CIENCIAS_DA_NATUREZA, 1, "Citologia, bioquímica e origem da vida."));
        disciplinaRepository.save(new Disciplina(null, "História",
                AreaConhecimento.CIENCIAS_HUMANAS, 3, "Brasil República e História Contemporânea."));

        livroRepository.save(novoLivro("Dom Casmurro", 1899, 256, TipoMaterial.LIVRO, FormatoDigital.PDF,
                "https://biblioteca.escola.edu.br/acervo/dom-casmurro.pdf",
                "Bentinho narra sua história com Capitu e a dúvida sobre a traição.", machado, literatura));
        livroRepository.save(novoLivro("Memórias Póstumas de Brás Cubas", 1881, 208, TipoMaterial.LIVRO, FormatoDigital.EPUB,
                "https://biblioteca.escola.edu.br/acervo/memorias-postumas.epub",
                "Um defunto autor conta a própria vida de forma irônica.", machado, literatura));
        livroRepository.save(novoLivro("O Cortiço", 1890, 304, TipoMaterial.LIVRO, FormatoDigital.PDF,
                "https://biblioteca.escola.edu.br/acervo/o-cortico.pdf",
                "Retrato naturalista da vida em um cortiço no Rio de Janeiro.", aluisio, literatura));
        livroRepository.save(novoLivro("Apostila de Funções do 1º Grau", 2025, 48, TipoMaterial.APOSTILA, FormatoDigital.PDF,
                "https://biblioteca.escola.edu.br/acervo/apostila-funcoes.pdf",
                "Teoria e exercícios resolvidos de função afim.", equipe, matematica));
        livroRepository.save(novoLivro("Resumo: Citologia", 2025, 12, TipoMaterial.RESUMO, FormatoDigital.HTML,
                "https://biblioteca.escola.edu.br/acervo/resumo-citologia.html",
                "Resumo das organelas celulares e suas funções.", equipe, biologia));
    }

    private User novoUsuario(String nome, String email) {
        User user = new User();
        user.setName(nome);
        user.setEmail(email);
        user.setPassword("123456");
        return user;
    }

    private Livro novoLivro(String titulo, int ano, int paginas, TipoMaterial tipo, FormatoDigital formato,
                            String url, String sinopse, Autor autor, Disciplina disciplina) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAnoPublicacao(ano);
        livro.setNumeroPaginas(paginas);
        livro.setTipo(tipo);
        livro.setFormato(formato);
        livro.setUrlAcesso(url);
        livro.setSinopse(sinopse);
        livro.setTotalAcessos(0);
        livro.setDataCadastro(LocalDate.now());
        livro.setAutor(autor);
        livro.setDisciplina(disciplina);
        return livro;
    }
}
