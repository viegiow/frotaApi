package com.example.frota.parametros;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frota.errors.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parametro")
@CrossOrigin("*")
public class ParametroController {
	@Autowired
	private ParametroService parametroService;
	
	@GetMapping                 
	public List<Parametro> listarCaixa (){
	    return parametroService.procurarTodas();
	}
	
	@GetMapping("/{id}")        
	public Parametro procurarParametro (@PathVariable("id") Long id){
		Parametro parametro = parametroService.procurarPorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Parametro não encontrado"));
		return parametro;
	}
	
//	@PostMapping
//	@Transactional
//	public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroCaixa dados) {
//		parametroService.salvar(dados);
//		return ResponseEntity.ok("Caixa criada com sucesso!");
//	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@RequestBody @Valid AtualizacaoParametro dados) {
		parametroService.atualizar(dados);
		return ResponseEntity.ok("Parametro atualizado com sucesso!");
	}
	
}
