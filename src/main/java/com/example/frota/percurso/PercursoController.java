package com.example.frota.percurso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frota.caminhao.CaminhaoService;

@RestController
@RequestMapping("/percursos")
@CrossOrigin("*")
public class PercursoController {
	@Autowired
	private CaminhaoService caminhaoService;
	
	@Autowired
	private PercursoService percursoService;

    @Autowired
    private PercursoRepository percursoRepository;

    @PostMapping("/criar")
    public ResponseEntity<?> criarPercursos() {
    	caminhaoService.lotarCaminhao();
        return ResponseEntity.ok("Percursos criados com sucesso");
    }
    @PostMapping("/iniciar/{id}")
    public ResponseEntity<?> iniciar(@PathVariable Long id) {
        percursoService.iniciarPercurso(id);
        return ResponseEntity.ok("Percurso iniciado com sucesso");
    }

    @PutMapping("/finalizar")
    public ResponseEntity<?> finalizar(@RequestBody FinalizarPercurso dados) {
    	Percurso percursoProcurado = percursoService.procurarPercursoPorId(dados.percursoId());
    	caminhaoService.atualizarKmChegada(dados.kmChegada(), percursoProcurado.getCaminhao().getId());
        Percurso percurso = percursoService.finalizarPercurso(dados);
        return ResponseEntity.ok(percurso);
    }

    @GetMapping("/{caminhaoId}")
    public List<Percurso> listar(@PathVariable Long caminhaoId) {
    	return percursoRepository.findAllByCaminhaoId(caminhaoId);
    }
}
