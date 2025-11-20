package com.example.frota.caminhao;


import java.util.List;
import java.util.Set;

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
@RequestMapping("/caminhao")
@CrossOrigin("*")
public class CaminhaoController {
	
	private final Set<String> CHAVES_VALIDAS = Set.of(
			"cco123",
			"azul123"
	);
	@Autowired
	private CaminhaoService caminhaoService;
	
	@GetMapping                 
	public List<Caminhao> ListaCaminhao (){
	    return caminhaoService.procurarTodos();
	}
	
	@GetMapping("/{id}")        
	public Caminhao procurarCaminhao (@PathVariable("id") Long id){
		Caminhao caminhao = caminhaoService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));
		return caminhao;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroCaminhao dados) {
		caminhaoService.salvar(dados);
		return ResponseEntity.ok("Caminhão criado com sucesso");
	}
	
//	//com segurança
//	@PostMapping
//	@Transactional
//	public ResponseEntity<?> cadastrar(
//		@RequestHeader("X-API-KEY") String apiKey,
//		@RequestBody @Valid CadastroCaminhao dados) {
//		//solução com alto acoplamento, pois o controller tem que saber muita coisa
//		//viola 3 principios do SOLID SRP, DIP, OCP
//		//ideal eh desmembrar o teste para outra classe
//		
//			//validar chave que veio do frontend
//		if (!CHAVES_VALIDAS.contains(apiKey)) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//					.body("{\"erro\":\"Chave API inválida\"}");
//		}
//		Marca marca = marcaService.procurarPorId(dados.marcaId());
//		Caminhao caminhao = new Caminhao(dados, marca);
//		caminhaoService.salvar(caminhao);
//		//controller sabe muito sobre validacao
//		return ResponseEntity.status(HttpStatus.CREATED).body(dados);
//	}
		
//		@PostMapping
//	    @ApiKeyRequired(permissions = {"WRITE"}, description = "Criar aluno")
//	    @RateLimit(value = 10, duration = 1, timeUnit = TimeUnit.MINUTES)
//	    public void criarAluno(@RequestBody @Valid DadosCadastroAluno dados) {
//			alunoService.salvar(dados);
//	    }

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@RequestBody @Valid AtualizacaoCaminhao dados) {
		caminhaoService.procurarPorId(dados.id())
			.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));
		caminhaoService.atualizar(dados);
		return ResponseEntity.ok("Caminhão atualizado com sucesso");
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		caminhaoService.procurarPorId(id)
			.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));
		caminhaoService.apagarPorId(id);
		return ResponseEntity.ok("Caminhão deletado com sucesso");
	}
	

}
