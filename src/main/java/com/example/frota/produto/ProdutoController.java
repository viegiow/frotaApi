package com.example.frota.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
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
				.orElseThrow(() -> new EntityNotFoundException("Marca não encontrado"));
		return produto;
	}
	
	@PostMapping
	@Transactional
	public void cadastrar(@RequestBody @Valid CadastroProduto dados) {
		produtoService.salvar(dados);
	}

	@PutMapping
	@Transactional
	public void atualizar (@RequestBody AtualizacaoProduto dados) {
		produtoService.atualizar(dados);
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		produtoService.apagarPorId(id);
	}
	
}
