package com.testes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testes.UserService;
import com.testes.modelos.Usuario;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Usuario user) {
        userService.createUser(user);
        return ResponseEntity.ok("Usuário criado com sucesso.");
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        Usuario user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody Usuario user) {
        Usuario existingUser = userService.getUserById(id);
        if (existingUser != null) {
            user.setId(id); 
            userService.updateUser(user);
            return ResponseEntity.ok("Usuário atualizado com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Usuario existingUser = userService.getUserById(id);
        if (existingUser != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable Long id) {
        Usuario user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user.getEmail());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
