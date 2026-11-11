package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.UserDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários (Estudantes)", description = "CRUD de usuário desenvolvido em aula (não contabilizado nos 5 CRUDs). Os usuários representam os estudantes.")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Cadastra um usuário/estudante")
    @PostMapping
    public UserLowDTO create(@RequestBody UserDTO user){
        return service.create(user);
    }

    @Operation(summary = "Atualiza um usuário (o ID vai no corpo da requisição)")
    @PutMapping
    public User update(@RequestBody User user){
        return service.update(user);
    }

    @Operation(summary = "Lista os usuários/estudantes")
    @GetMapping
    public List<UserLowDTO> findAll(){
        return service.findUsers();
    }

    @Operation(summary = "Exclui um usuário pelo ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }





}
