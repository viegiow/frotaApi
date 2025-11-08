package com.example.frota.produto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoMapper produtoMapper;
	
	public Produto salvarOuAtualizar(AtualizacaoProduto dto) {
        if (dto.id() != null) {
            Produto existente = produtoRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            produtoMapper.updateEntityFromDto(dto, existente);
            return produtoRepository.save(existente);
        } else {
            Produto novoProduto = produtoMapper.toEntityFromAtualizacao(dto);
            
            return produtoRepository.save(novoProduto);
        }
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
