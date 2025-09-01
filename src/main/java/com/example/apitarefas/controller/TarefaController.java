package com.example.apitarefas.controller;

import com.example.apitarefas.model.Tarefa;
import com.example.apitarefas.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    // Listar todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefas() {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarTarefaPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        return tarefa.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        Optional<Tarefa> tarefaExistente = tarefaRepository.findById(id);

        if (tarefaExistente.isPresent()) {
            Tarefa tarefa = tarefaExistente.get();
            tarefa.setNome(tarefaAtualizada.getNome());
            tarefa.setDataEntrega(tarefaAtualizada.getDataEntrega());
            tarefa.setResponsavel(tarefaAtualizada.getResponsavel());

            Tarefa tarefaSalva = tarefaRepository.save(tarefa);
            return new ResponseEntity<>(tarefaSalva, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}