package com.example.frota.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping                 
	public List<Produto> listarProduto (){
	    return produtoService.procurarTodas();
	}
	
	@GetMapping("/{id}")        
	public Produto procurarProduto (@PathVariable("id") Long id){
		Produto produto = produtoService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		return produto;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroProduto dados) {
		produtoService.salvar(dados);
		return ResponseEntity.ok("Produto criado com sucesso!");
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@RequestBody @Valid AtualizacaoProduto dados) {
		produtoService.atualizar(dados);
		return ResponseEntity.ok("Produto atualizado com sucesso!");
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		produtoService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
		produtoService.apagarPorId(id);
		return ResponseEntity.ok("Produto deletado com sucesso!");
	}
	
}
