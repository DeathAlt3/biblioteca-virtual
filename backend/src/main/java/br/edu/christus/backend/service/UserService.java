package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.UserDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.repository.UserRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    // Recebe UserDTO e devolve UserLowDTO.
    public UserLowDTO create(UserDTO dto){
        //Converte dto em Entity
        var entity = MapperUtil.parseObject(dto, User.class);
        //Salva a entidade e retorna ela com ID
        var entitySaved = repository.save(entity);
        // Converte a entidade salva em UserLowDTO
        var dtoResponse = MapperUtil.parseObject(entitySaved, UserLowDTO.class);
        //Retorna o DTO convertido
        return dtoResponse;
    }

    public UserLowDTO createV2(UserDTO dto){
        var entity = MapperUtil.parseObject(dto, User.class);
        return MapperUtil.parseObject(
                repository.save(entity), UserLowDTO.class);
    }

    public User update(User entity){
        if(entity.getId() == null){
            return null;
        }
        return repository.save(entity);
    }

    public List<UserLowDTO> findUsers(){
        return MapperUtil.parseListObjects(
                repository.findAll(), UserLowDTO.class);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }





}
