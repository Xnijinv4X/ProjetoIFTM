package com.example.projeto.games.controller;

import com.example.projeto.games.model.GamesModel;
import com.example.projeto.games.repository.GamesRepository;
import com.example.projeto.login.dto.CreateUserDTO;
import com.example.projeto.login.model.ModelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projeto.games.dto.GamesDTO;
import com.example.projeto.games.service.GamesService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GamesController {

    @Autowired
    GamesService gamesService;

    @Autowired
    GamesRepository gamesRepository;

    @PostMapping("/jogos")
    public ResponseEntity<String> cadastrarJogo(@RequestBody GamesDTO gamesDTO) {
        try {
            if (gamesDTO.nome().isEmpty() || gamesDTO.descricao().isEmpty()
                    || gamesDTO.local().isEmpty() || gamesDTO.responsaveis().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha todos os dados");
            }

            gamesService.salvarJogo(gamesDTO);
            return ResponseEntity.status(201).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar jogo: " + e.getMessage());
        }
    }

    @PutMapping("/jogos/atualizar/{id}")
    public ResponseEntity<String> atualizarJogo(@PathVariable Long id, @RequestBody GamesDTO gamesDto) {
        try {
            if (gamesRepository.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Jogo não encontrado");
            }
            if (gamesDto.nome().isEmpty() || gamesDto.descricao().isEmpty() || gamesDto.local().isEmpty() || gamesDto.responsaveis().isEmpty() || gamesDto.situacao().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha todos os dados");
            }
            gamesService.atualizarJogo(id, gamesDto);
            return ResponseEntity.status(200).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar jogo: " + e.getMessage());
        }
    }

    @GetMapping("/jogos/listar")
    public ResponseEntity<List<GamesModel>> listaJogos() {
        return ResponseEntity.status(200).body(gamesService.listarJogos());
    }

    @DeleteMapping("admin/jogos/remover/{id}")
    public ResponseEntity<String> removerUsuario(@PathVariable Long id) {
        if (gamesService.removerJogo(id)) {
            return ResponseEntity.status(200).body(null);
        } else {
            return ResponseEntity.status(500).body("Jogo não encontrado");
        }
    }

}
