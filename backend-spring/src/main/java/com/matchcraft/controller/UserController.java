package com.matchcraft.controller;

import com.matchcraft.dto.UserDto;
import com.matchcraft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*") // Autoriser toutes les origines pour le developpement
public class UserController {

    @Autowired
    private UserService userService;

    // Récupérer le profil courant (ID 1 fixé pour l'instant)
    @GetMapping
    public ResponseEntity<UserDto> getProfile() {
        return ResponseEntity.ok(userService.getUserById(1L));
    }

    // Mettre à jour le profil
    @PutMapping
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(1L, dto));
    }

    // Déclencher la synchronisation GitHub
    @PostMapping("/sync-github")
    public ResponseEntity<Void> syncGithub() {
        userService.syncGithub(1L);
        return ResponseEntity.ok().build();
    }
}
