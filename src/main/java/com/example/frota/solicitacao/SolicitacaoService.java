package com.example.frota.solicitacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.example.frota.frete.FreteService;
import com.example.frota.produto.Produto;
import com.example.frota.produto.ProdutoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SolicitacaoService {
	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@Autowired
	private SolicitacaoMapper solicitacaoMapper;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private CaixaService caixaService;
	
	@Autowired
	private FreteService freteService;

	public Solicitacao salvarOuAtualizar(AtualizacaoSolicitacao dto) {
		// Valida se a marca existe
		Produto produto = produtoService.procurarPorId(dto.produtoId())
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + dto.produtoId()));
		Caixa caixa = caixaService.procurarPorId(dto.caixaId())
				.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrada com ID: " + dto.caixaId()));
		
		Double custoFrete = freteService.obterTotalFrete(dto.enderecoPartida(), dto.enderecoDestino(), dto.produtoId(), dto.caixaId());
		
		if (dto.id() != null) {
			// atualizando Busca existente e atualiza
			Solicitacao existente = solicitacaoRepository.findById(dto.id())
					.orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada com ID: " + dto.id()));
			solicitacaoMapper.updateEntityFromDto(dto, existente);
			existente.setProduto(produto); // Atualiza a marca
			existente.setCaixa(caixa);
			existente.setFrete(custoFrete);
			
			return solicitacaoRepository.save(existente);
		} else {
			Solicitacao novaSolicitacao = solicitacaoMapper.toEntityFromAtualizacao(dto);
			novaSolicitacao.setProduto(produto);
			novaSolicitacao.setCaixa(caixa);
			novaSolicitacao.setFrete(custoFrete);
			
			return solicitacaoRepository.save(novaSolicitacao);
		}
	}
	
	public List<Solicitacao> procurarTodos(){
		return solicitacaoRepository.findAll(Sort.by("frete").ascending());
	}
	public void apagarPorId (Long id) {
		solicitacaoRepository.deleteById(id);
	}

	public Optional<Solicitacao> procurarPorId(Long id) {
		return solicitacaoRepository.findById(id);
	}
}
