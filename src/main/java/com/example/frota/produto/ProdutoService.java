package com.example.frota.produto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto salvar(CadastroProduto dados) {
		Produto novoProduto = new Produto(dados);
		return produtoRepository.save(novoProduto);
	}
	public void atualizar(AtualizacaoProduto dados) {
		Produto atualizarProduto = produtoRepository.getReferenceById(dados.id());
		atualizarProduto.atualizar(dados);
		produtoRepository.save(atualizarProduto);
	}
	public List<Produto> buscarProdutosSemSolicitacao() {
	    return produtoRepository.findProdutosSemSolicitacao();
	}

	public List<Produto> procurarTodas() {
		return produtoRepository.findAll();
	}
	
	public Optional<Produto> procurarPorId(Long id) {
		return produtoRepository.findById(id);
	}
	
	public void apagarPorId (Long id) {
		produtoRepository.deleteById(id);
	}
}
