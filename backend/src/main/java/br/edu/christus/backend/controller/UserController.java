package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.UserDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public UserLowDTO create(@RequestBody UserDTO user){
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user){
        return service.update(user);
    }

    @GetMapping
    public List<UserLowDTO> findAll(){
        return service.findUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }





}
