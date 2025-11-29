package com.example.frota.percurso;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/percursos")
@CrossOrigin("*")
public class PercursoController {

    @Autowired
    private PercursoService percursoService;

    @Autowired
    private PercursoRepository percursoRepository;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciar(@RequestBody CadastroPercurso dados) {
        percursoService.iniciarPercurso(dados);
        return ResponseEntity.ok("Percurso cadastrado e iniciado com sucesso");
    }

    @PutMapping("/finalizar")
    public ResponseEntity<?> finalizar(@RequestBody FinalizarPercurso dados) {
        Percurso percurso = percursoService.finalizarPercurso(dados);
        return ResponseEntity.ok(percurso);
    }

    @GetMapping("/{caminhaoId}")
    public List<Percurso> listar(@PathVariable Long caminhaoId) {
    	return percursoRepository.findAllByCaminhaoId(caminhaoId);
    }
}
