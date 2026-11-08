# 📚 API REST - Biblioteca Pública Virtual para Ensino Médio

Backend desenvolvido para a avaliação **NP2** da disciplina de Tecnologia e Programação Web.

O objetivo do sistema é organizar, disponibilizar e consultar livros e materiais didáticos 100% digitais (e-books, PDFs, apostilas) para estudantes do ensino médio, acompanhando o progresso de leitura escolar.

---

## 👥 Equipe e Responsabilidades

* **Alan Sávio Lima Benício** ([@DeathAlt3](https://github.com/DeathAlt3)) — **Integrante A (Catálogo & Acervo Digital):**
  * CRUD completo de **Autor** (`tb_autor`).
  * CRUD completo de **Disciplina** (`tb_disciplina`).
  * CRUD central de **Livro / Material Digital** (`tb_livro`) com relacionamentos, contadores de acesso e formatos (PDF, EPUB).
  * Validações de entrada com Bean Validation.

* **Integrante B** — **Integrante B (Operações & Experiência do Estudante):**
  * CRUD completo de **Avaliação / Resenha** (`tb_avaliacao`).
  * CRUD completo de **Registro de Leitura Digital** (`tb_registro_leitura`).
  * **Funcionalidade além do CRUD:** Acompanhamento inteligente de progresso de estudo com cálculo automático de percentual lido e estimativa de dias para conclusão.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17 / 21
* **Framework:** Spring Boot (Spring Web MVC, Spring Data JPA)
* **Validação:** Jakarta Bean Validation
* **Bancos de Dados:** H2 Database (em memória) e PostgreSQL
* **Mapeamento de Objetos:** ModelMapper
* **Documentação de API:** SpringDoc OpenAPI 3 / Swagger UI

---

## 🏛️ Camadas da Aplicação

* **`controller`:** Endpoints REST e anotações do Swagger.
* **`domain/dto`:** Objetos de transferência de dados com validações (`@NotBlank`, `@NotNull`, etc.).
* **`domain/model`:** Entidades JPA mapeadas para as tabelas do banco de dados.
* **`repository`:** Interfaces que herdam de `JpaRepository` para persistência.
* **`service`:** Lógica de negócio e regras de validação.
* **`exception`:** Tratamento global de erros (`GlobalExceptionHandler`).

---

## 🚀 Como Executar o Projeto

### 1. Clonar o repositório
```bash
git clone https://github.com/DeathAlt3/biblioteca-virtual-backend.git
cd biblioteca-virtual-backend/backend
```

### 2. Iniciar a Aplicação (Banco H2 em memória)
* **No Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
* **No VS Code:**
  Abra o arquivo `BackendApplication.java` e clique no botão **Run** acima do método `main`.

A aplicação já inicializa automaticamente com materiais didáticos de exemplo cadastrados para testes (`DataInitializer`).

---

## 📖 Documentação das Rotas (Swagger UI)

Com a aplicação em execução, acesse no navegador:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Resumo dos Endpoints:
* **Autores:** `/api/v1/autores` (cadastrar, listar, buscar por ID, atualizar, deletar)
* **Disciplinas:** `/api/v1/disciplinas` (cadastrar, listar, buscar por ID, atualizar, deletar)
* **Livros Digitais:** `/api/v1/livros` (cadastrar, listar com filtros, buscar por ID, atualizar, registrar acesso, deletar)
* **Avaliações:** `/api/v1/avaliacoes` (cadastrar resenha, listar, buscar por ID, atualizar, deletar)
* **Registros de Leitura:** `/api/v1/leituras` (iniciar sessão de leitura, listar histórico, buscar por ID, deletar)
  * ⭐ `PATCH /api/v1/leituras/{id}/progresso`: **Funcionalidade além do CRUD** que registra páginas lidas, tempo de estudo, calcula automaticamente o percentual concluído e estima os dias restantes para término da leitura.
