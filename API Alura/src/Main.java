public class Main {
    public static void main(String[] args) {
        System.out.println("API Alura!");
    }
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console

    package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

    @Entity
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        // Getters and setters
    }
    UserRepository.java
            java
    Copiar código
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepository extends JpaRepository<User, Long> {
        User findByEmail(String email);
    }
    package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

    @Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public Optional<User> getUserById(Long id) {
            return userRepository.findById(id);
        }

        public User createUser(User user) {
            return userRepository.save(user);
        }

        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
        package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

        @RestController
        @RequestMapping("/api/users")
        public class UserController {

            @Autowired
            private UserService userService;

            @GetMapping
            public List<User> getAllUsers() {
                return userService.getAllUsers();
            }

            @GetMapping("/{id}")
            public ResponseEntity<User> getUserById(@PathVariable Long id) {
                Optional<User> user = userService.getUserById(id);
                return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
            }

            @PostMapping
            public User createUser(@Valid @RequestBody User user) {
                return userService.createUser(user);
            }

            @DeleteMapping("/{id}")
            public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
                userService.deleteUser(id);
                return ResponseEntity.noContent().build();
            }
    }

}