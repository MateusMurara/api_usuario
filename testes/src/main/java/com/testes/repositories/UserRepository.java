package com.testes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testes.modelos.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
   Optional<Usuario> findUserByEmail(String email);
}
