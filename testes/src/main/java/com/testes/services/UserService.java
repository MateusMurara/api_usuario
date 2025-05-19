package com.testes.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.testes.modelos.Usuario;
import com.testes.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Usuario getUserEmail(String email) {
    	Optional<Usuario> opt = userRepository.findUserByEmail(email);
        
    	if(Objects.nonNull(opt) && opt.isPresent()) {
    		return opt.get();
    	}
    	return null;
    }
    
    public void createUser(Usuario user) {
        userRepository.save(user);
    }

    public Usuario getUserById(Long id) {
    	Optional<Usuario> opt = userRepository.findById(id);
        return opt.orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    }

    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Usuario user) {
    	Optional<Usuario> opt = userRepository.findById(user.getId());
        Usuario userOld = opt.orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        userOld.setEmail(user.getEmail());
        userOld.setNome(user.getNome());
        userOld.setPassword(user.getPassword());
        
        userRepository.save(userOld);
    }

    
    public void deleteUser(Long id) {
    	Optional<Usuario> opt = userRepository.findById(id);
        Usuario userOld = opt.orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    
    	userRepository.delete(userOld);
    }  
}
