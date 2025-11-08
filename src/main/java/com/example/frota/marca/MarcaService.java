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
	
	@Autowired
	private MarcaMapper marcaMapper;
	
	public Marca salvarOuAtualizar(DadosAtualizacaoMarca dto) {
        if (dto.id() != null) {
            // atualizando Busca existente e atualiza
            Marca existente = marcaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            marcaMapper.updateEntityFromDto(dto, existente);
            return marcaRepository.save(existente);
        } else {
            // criando Novo caminhão
            Marca novaMarca = marcaMapper.toEntityFromAtualizacao(dto);
            return marcaRepository.save(novaMarca);
        }
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
