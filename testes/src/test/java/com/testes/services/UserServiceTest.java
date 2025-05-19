package com.testes.services;

import com.testes.modelos.Usuario;
import com.testes.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserEmail_found() {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "1234");
        when(userRepository.findUserByEmail("joao@email.com")).thenReturn(Optional.of(user));

        Usuario result = userService.getUserEmail("joao@email.com");

        assertNotNull(result);
        assertEquals("Joao", result.getNome());
    }

    @Test
    void getUserEmail_notFound() {
        when(userRepository.findUserByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        Usuario result = userService.getUserEmail("naoexiste@email.com");

        assertNull(result);
    }

    @Test
    void createUser() {
        Usuario user = new Usuario(null, "Maria", "maria@email.com", "1234");
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_found() {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Usuario result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Joao", result.getNome());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
        assertEquals("Usuario não encontrado", ex.getMessage());
    }

    @Test
    void getAllUsers() {
        List<Usuario> users = Arrays.asList(
                new Usuario(1L, "Joao", "joao@email.com", "1234"),
                new Usuario(2L, "Maria", "maria@email.com", "5678")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<Usuario> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void updateUser_found() {
        Usuario existing = new Usuario(1L, "Joao", "joao@email.com", "1234");
        Usuario update = new Usuario(1L, "Joao Atualizado", "novo@email.com", "9999");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        userService.updateUser(update);

        verify(userRepository).save(existing);
        assertEquals("Joao Atualizado", existing.getNome());
        assertEquals("novo@email.com", existing.getEmail());
        assertEquals("9999", existing.getPassword());
    }

    @Test
    void updateUser_notFound() {
        Usuario update = new Usuario(1L, "Joao Atualizado", "novo@email.com", "9999");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(update));
        assertEquals("Usuario não encontrado", ex.getMessage());
    }

    @Test
    void deleteUser_found() {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("Usuario não encontrado", ex.getMessage());
    }
}