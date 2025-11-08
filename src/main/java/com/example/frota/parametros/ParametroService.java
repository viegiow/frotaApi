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
	
	@Autowired
	private ParametroMapper parametroMapper;
	
	public Parametro salvarOuAtualizar(AtualizacaoParametro dto) {
        if (dto.id() != null) {
            Parametro existente = parametroRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado com ID: " + dto.id()));
            parametroMapper.updateEntityFromDto(dto, existente);
            return parametroRepository.save(existente);
        } else {
            Parametro novoParametro = parametroMapper.toEntityFromAtualizacao(dto);
            
            return parametroRepository.save(novoParametro);
        }
    }
	
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
}
