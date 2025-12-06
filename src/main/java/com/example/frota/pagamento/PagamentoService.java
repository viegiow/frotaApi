package com.example.frota.pagamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.solicitacao.Solicitacao;
import com.example.frota.solicitacao.SolicitacaoService;



@Service
public class PagamentoService {
	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private PagamentoRepository pagamentoRepo;
	
	public List<Pagamento> procurarTodos(){
		return pagamentoRepo.findAll();
	}

	public Optional<Pagamento> procurarPorId(Long id) {
		return pagamentoRepo.findById(id);
	}

	public void salvar(CadastroPagamento dados) {

		Solicitacao solicitacao = solicitacaoService.procurarPorId(dados.solicitacao_id()).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		Pagamento novoPagamento = new Pagamento(dados, solicitacao);
		Pagamento pagamentoProcessado = processarPagamento(novoPagamento); 
		pagamentoRepo.save(pagamentoProcessado);
	}

	public Pagamento processarPagamento(Pagamento pagamento) {
		pagamento.setDataPagamento(LocalDateTime.now());
		pagamento.setAprovado(true);
		return pagamento;
	}

	public void atualizar(AtualizacaoPagamento dados) {
		Pagamento pagamentoExistente = pagamentoRepo.findById(dados.id())
				.orElseThrow(() -> new ResourceNotFoundException("pagamento não encontrado"));
		pagamentoExistente.atualizarPagamento(dados);
		pagamentoRepo.save(pagamentoExistente);
	}

	public void apagarPorId (Long id) {
		pagamentoRepo.deleteById(id);
	}
}
