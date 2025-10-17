package com.example.frota.caixa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CaixaService {
	@Autowired
	private CaixaRepository caixaRepository;
	
	@Autowired CaixaMapper caixaMapper;
	
	public Caixa salvarOuAtualizar(AtualizacaoCaixa dto) {
        if (dto.id() != null) {
            Caixa existente = caixaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            caixaMapper.updateEntityFromDto(dto, existente);
            return caixaRepository.save(existente);
        } else {
            Caixa novaCaixa = caixaMapper.toEntityFromAtualizacao(dto);
            
            return caixaRepository.save(novaCaixa);
        }
    }
	
	public List<Caixa> procurarTodas() {
		return caixaRepository.findAll();
	}
	
	public Optional<Caixa> procurarPorId(Long id) {
		return caixaRepository.findById(id);
	}
	
	public void apagarPorId (Long id) {
		caixaRepository.deleteById(id);
	}

}
