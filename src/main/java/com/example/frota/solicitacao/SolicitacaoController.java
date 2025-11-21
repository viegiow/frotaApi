package com.example.frota.solicitacao;

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
@RequestMapping("/solicitacao")
@CrossOrigin("*")
public class SolicitacaoController {
	@Autowired
	private SolicitacaoService solicitacaoService;
	
	@GetMapping                 
	public List<Solicitacao> listarSolicitacoes (){
	    return solicitacaoService.procurarTodos();
	}
	
	@GetMapping("/{id}")        
	public Solicitacao procurarSolicitacoes (@PathVariable("id") Long id){
		Solicitacao solicitacao = solicitacaoService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Solicitacao não encontrada"));
		return solicitacao;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroSolicitacao dados) {
		solicitacaoService.salvar(dados);
		return ResponseEntity.ok("Solicitacao criada com sucesso!");
		
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@RequestBody @Valid AtualizacaoSolicitacao dados) {
		solicitacaoService.atualizar(dados);
		return ResponseEntity.ok("Solicitacao atualizada com sucesso!");
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		solicitacaoService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("Solicitacao não encontrada."));
		solicitacaoService.apagarPorId(id);
		return ResponseEntity.ok("Solicitacao deletada com sucesso!");
	}

}
