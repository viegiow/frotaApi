package com.example.frota.solicitacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.frete.FreteCustoDistancia;
import com.example.frota.frete.FreteService;
import com.example.frota.produto.Produto;
import com.example.frota.produto.ProdutoService;

@Service
public class SolicitacaoService {
	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private CaixaService caixaService;
	
	@Autowired
	private FreteService freteService;

	
	public List<Solicitacao> procurarTodos(){
		return solicitacaoRepository.findAll(Sort.by("frete").ascending());
	}
	public void apagarPorId (Long id) {
		solicitacaoRepository.deleteById(id);
	}

	public Optional<Solicitacao> procurarPorId(Long id) {
		return solicitacaoRepository.findById(id);
	}

	public void salvar(CadastroSolicitacao dados) {
		Produto produto = produtoService.procurarPorId(dados.produtoId())
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		Caixa caixa = caixaService.procurarPorId(dados.caixaId())
				.orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada"));
		FreteCustoDistancia dadosFrete = new FreteCustoDistancia(dados.enderecoPartida(), dados.enderecoDestino(),dados.produtoId(), dados.caixaId());
		Double custoFrete = freteService.obterTotalFrete(dadosFrete);
		Solicitacao novaSolicitacao = new Solicitacao(dados, produto, caixa, custoFrete, 0.0);
		solicitacaoRepository.save(novaSolicitacao);
		
	}
	public void atualizar(AtualizacaoSolicitacao dados) {
		Produto produto = produtoService.procurarPorId(dados.produtoId())
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		Caixa caixa = caixaService.procurarPorId(dados.caixaId())
				.orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada"));
		FreteCustoDistancia dadosFrete = new FreteCustoDistancia(dados.enderecoPartida(), dados.enderecoDestino(),dados.produtoId(), dados.caixaId());
		Double custoFrete = freteService.obterTotalFrete(dadosFrete);
		Solicitacao solicitacaoExistente = solicitacaoRepository.findById(dados.id())
				.orElseThrow(() -> new ResourceNotFoundException("Solicitacao não encontrada"));
		solicitacaoExistente.atualizarSolicitacao(dados, produto, caixa, custoFrete, custoFrete);
		solicitacaoRepository.save(solicitacaoExistente);
	}
}
