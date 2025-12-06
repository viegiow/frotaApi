package com.example.frota.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/feedback")
@CrossOrigin("*")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@GetMapping                 
	public List<Feedback> listarFeedbacks (){
	    return feedbackService.procurarTodos();
	}
	
	@GetMapping("/{id}")        
	public Feedback procurarFeedbacks (@PathVariable("id") Long id){
		Feedback feedback = feedbackService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("feedback não encontrado"));
		return feedback;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroFeedback dados) {
		feedbackService.salvar(dados);
		return ResponseEntity.ok("feedback criada com sucesso!");
	}
	
	
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		feedbackService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("feedback não encontrada."));
		feedbackService.apagarPorId(id);
		return ResponseEntity.ok("feedback deletada com sucesso!");
	}
}
