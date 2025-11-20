package com.example.frota.caixa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frota.errors.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/caixa")
@CrossOrigin("*")
public class CaixaController {
	@Autowired
	private CaixaService caixaService;
	
	@GetMapping                 
	public List<Caixa> listarCaixa (){
	    return caixaService.procurarTodas();
	}
	
	@GetMapping("/{id}")        
	public Caixa procurarCaixa (@PathVariable("id") Long id){
		Caixa caixa = caixaService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada"));
		return caixa;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroCaixa dados) {
		caixaService.salvar(dados);
		return ResponseEntity.ok("Caixa criada com sucesso!");
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@RequestBody @Valid AtualizacaoCaixa dados) {
		caixaService.atualizar(dados);
		return ResponseEntity.ok("Caixa atualizada com sucesso!");
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		caixaService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada."));
		caixaService.apagarPorId(id);
		return ResponseEntity.ok("Caixa deletada com sucesso!");
	}
}
