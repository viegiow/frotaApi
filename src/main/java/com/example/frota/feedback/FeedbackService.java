package com.example.frota.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.errors.ResourceNotFoundException;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepo;
	
	public List<Feedback> procurarTodos(){
		return feedbackRepo.findAll();
	}

	public Optional<Feedback> procurarPorId(Long id) {
		return feedbackRepo.findById(id);
	}

	public void salvar(CadastroFeedback dados, String tipo) {
		Feedback novoFeedback = new Feedback(dados, tipo);
		feedbackRepo.save(novoFeedback);
		
	}
	public void atualizar(AtualizacaoFeedback dados) {
		Feedback feedbackExistente = feedbackRepo.findById(dados.id())
				.orElseThrow(() -> new ResourceNotFoundException("Feedback não encontrado"));
		feedbackExistente.atualizarFeedback(dados);
		feedbackRepo.save(feedbackExistente);
	}
}
