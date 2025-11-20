package com.example.frota.parametros;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ParametroService {
	@Autowired
	private ParametroRepository parametroRepository;
	
	public List<Parametro> procurarTodas() {
		return parametroRepository.findAll();
	}
	
	public Optional<Parametro> procurarPorId(Long id) {
		return parametroRepository.findById(id);
	}

	public Optional<Parametro> procurarPorNome(String nome) {
		return parametroRepository.findByNome(nome);
	}
	
	public void apagarPorId (Long id) {
		parametroRepository.deleteById(id);
	}

	public void atualizar(AtualizacaoParametro dados) {
		Parametro parametro = parametroRepository.findById(dados.id())
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado"));
		parametro.atualizar(dados);
		parametroRepository.save(parametro);
	}
}
