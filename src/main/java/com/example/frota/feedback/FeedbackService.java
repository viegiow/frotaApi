package com.example.frota.feedback;

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
public class FeedbackService {
	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private FeedbackRepository feedbackRepo;
	
	public List<Feedback> procurarTodos(){
		return feedbackRepo.findAll();
	}

	public Optional<Feedback> procurarPorId(Long id) {
		return feedbackRepo.findById(id);
	}

	public void salvar(CadastroFeedback dados) {

		Solicitacao solicitacao = solicitacaoService.procurarPorId(dados.solicitacao_id()).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
		Feedback novoFeedback = new Feedback(dados, solicitacao);
		feedbackRepo.save(novoFeedback);

		
	}

	public void atualizar(AtualizacaoFeedback dados) {
		Feedback feedbackExistente = feedbackRepo.findById(dados.id())
				.orElseThrow(() -> new ResourceNotFoundException("Feedback não encontrado"));
		feedbackExistente.atualizarFeedback(dados);
		feedbackRepo.save(feedbackExistente);
	}

	public void apagarPorId (Long id) {
		feedbackRepo.deleteById(id);
	}
}
