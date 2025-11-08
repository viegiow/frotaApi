package com.example.frota.marca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/marca")
@CrossOrigin("*")
public class MarcaController {
	@Autowired
	private MarcaService marcaService;
	
	@GetMapping                 
	public List<Marca> listarMarca (){
	    return marcaService.procurarTodos();
	}
	
	@GetMapping("/{id}")        
	public Marca procurarMarca (@PathVariable("id") Long id){
		Marca marca = marcaService.procurarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Marca não encontrado"));
		return marca;
	}
	
	@PostMapping
	@Transactional
	public void cadastrar(@RequestBody @Valid DadosCadastroMarca dados) {
		marcaService.salvar(dados);
	}

	@PutMapping
	@Transactional
	public void atualizar (@RequestBody DadosAtualizacaoMarca dados) {
		marcaService.atualizar(dados);
	}
	
	@DeleteMapping ("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		marcaService.apagarPorId(id);
	}

}
