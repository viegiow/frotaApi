package com.example.frota.solicitacao;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.example.frota.errors.EntregaJaRealizada;
import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.errors.StatusErrado;
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
	public void processar(Long id) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		if (!solicitacao.getStatus().equals(StatusSolicitacao.COLETA)) { throw new StatusErrado("A solicitação não está no status correto para iniciar o processamento"); }
		else {
			solicitacao.setStatus(StatusSolicitacao.EM_PROCESSAMENTO);
			solicitacaoRepository.save(solicitacao);
		}
	}
	public void aCaminho(Long id) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		if (!solicitacao.getStatus().equals(StatusSolicitacao.EM_PROCESSAMENTO)) { throw new StatusErrado("A solicitação não está no status correto para iniciar o caminho de entrega"); }
		else {
			RestTemplate restTemplate = new RestTemplate();
			solicitacao.setStatus(StatusSolicitacao.A_CAMINHO);
			
			// enviar mensagem
			String to = "whatsapp:" + solicitacao.getTelefoneContato();
			String encoded = URLEncoder.encode(to, StandardCharsets.UTF_8);

			URI uri = UriComponentsBuilder
			        .fromUriString("http://localhost:8081/api/whatsapp/enviar")
			        .queryParam("to", encoded)
			        .build(true)
			        .toUri();

			restTemplate.postForObject(uri, null, String.class);
			solicitacaoRepository.save(solicitacao);
		}
	}
	public void entregar(Long id) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		if (!solicitacao.getStatus().equals(StatusSolicitacao.A_CAMINHO)) { throw new EntregaJaRealizada("A solicitação não está no status correto para finalizar a entrega"); }
		else {
			solicitacao.setStatus(StatusSolicitacao.ENTREGUE);
			solicitacaoRepository.save(solicitacao);
		}
	}
	public void cancelar(Long id, String motivo) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		solicitacao.setStatus(StatusSolicitacao.CANCELADO);
		solicitacao.setMotivoCancelamento(motivo);
		solicitacaoRepository.save(solicitacao);
	}
}
