package com.testes.controllers;

import com.testes.modelos.Usuario;
import com.testes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser() throws Exception {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "123456");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário criado com sucesso."));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(Usuario.class));
    }

    @Test
    void getAllUsers() throws Exception {
        List<Usuario> users = Arrays.asList(
                new Usuario(1L, "Joao", "joao@email.com", "123456"),
                new Usuario(2L, "Maria", "maria@email.com", "654321")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Joao")))
                .andExpect(jsonPath("$[1].nome", is("Maria")));
    }

    @Test
    void getUserById_found() throws Exception {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "123456");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Joao")));
    }

    @Test
    void getUserById_notFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(null);

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_found() throws Exception {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "123456");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário atualizado com sucesso."));

        Mockito.verify(userService).updateUser(Mockito.any(Usuario.class));
    }

    @Test
    void updateUser_notFound() throws Exception {
        Usuario user = new Usuario();
        when(userService.getUserById(1L)).thenReturn(null);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_found() throws Exception {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "123456");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso."));

        verify(userService).deleteUser(1L);
    }

    @Test
    void deleteUser_notFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(null);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserEmail_found() throws Exception {
        Usuario user = new Usuario(1L, "Joao", "joao@email.com", "123456");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1/email"))
                .andExpect(status().isOk())
                .andExpect(content().string("joao@email.com"));
    }

    @Test
    void getUserEmail_notFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1/email"))
                .andExpect(status().isNotFound());
    }

}