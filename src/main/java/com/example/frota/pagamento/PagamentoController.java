package com.example.frota.pagamento;

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
@RequestMapping("/pagamento")
@CrossOrigin("*")
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;
	
	@GetMapping                 
	public List<Pagamento> listarPagamentos (){
	    return pagamentoService.procurarTodos();
	}
	
	@GetMapping("/{id}")        
	public Pagamento procurarPagamento (@PathVariable("id") Long id){
		Pagamento pagamento = pagamentoService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("pagamento não encontrado"));
		return pagamento;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroPagamento dados) {
		pagamentoService.salvar(dados);
		return ResponseEntity.ok("pagamento criado com sucesso!");
	}

		
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		pagamentoService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("pagamento não encontrada."));
		pagamentoService.apagarPorId(id);
		return ResponseEntity.ok("pagamento deletada com sucesso!");
	}
}
