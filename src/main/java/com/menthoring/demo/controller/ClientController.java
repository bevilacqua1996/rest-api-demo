package com.menthoring.demo.controller;

import com.menthoring.demo.dto.ClienteDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listClients() {
        return ResponseEntity.ok(List.of(new ClienteDTO("João", "joao@email.com")));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> createClient(@RequestBody @Valid ClienteDTO cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
}

