package com.example.frota.marca;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.frota.caminhao.AtualizacaoCaminhao;
import com.example.frota.caminhao.Caminhao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

@Service
public class MarcaService {
	@Autowired
	private MarcaRepository marcaRepository;
	
	public void salvar(DadosCadastroMarca dados) {
		Marca novaMarca = new Marca(dados);
		marcaRepository.save(novaMarca);
	}
	public void atualizar(DadosAtualizacaoMarca dados) {
		Marca atualizarMarca = marcaRepository.getReferenceById(dados.id());
		atualizarMarca.atualizarInformacoes(dados);
		marcaRepository.save(atualizarMarca);
	}
	public List<Marca> procurarTodos(){
		return marcaRepository.findAll(Sort.by("nome").ascending());
	}
	public void apagarPorId (Long id) {
		marcaRepository.deleteById(id);
	}
	public Optional<Marca> procurarPorId( Long id) {
		return marcaRepository.findById(id);
	}
}
