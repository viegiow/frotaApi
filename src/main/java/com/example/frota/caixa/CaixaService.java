package com.example.frota.caixa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.parametros.Parametro;
import com.example.frota.parametros.ParametroService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CaixaService {
	@Autowired
	private CaixaRepository caixaRepository;

	@Autowired
	private ParametroService parametroService;
	
	@Autowired CaixaMapper caixaMapper;
	
	public Caixa salvarOuAtualizar(AtualizacaoCaixa dto) {
		Parametro parametro = parametroService.procurarPorNome("FATOR_CUBAGEM").orElseThrow(() -> new EntityNotFoundException("Parâmetro de custo por peso não encontrado"));;
        double fatorCubagem = Double.parseDouble(parametro.getValor());
        if (dto.id() != null) {
            Caixa existente = caixaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            caixaMapper.updateEntityFromDto(dto, existente);
            existente.calcularPesoCubadoComFator(fatorCubagem);
            return caixaRepository.save(existente);
        } else {
            Caixa novaCaixa = caixaMapper.toEntityFromAtualizacao(dto);
            novaCaixa.calcularPesoCubadoComFator(fatorCubagem);
            return caixaRepository.save(novaCaixa);
        }
    }
	
	public List<Caixa> procurarCompativeis(int comprimento, int altura, int largura, Double pesoMax) {
	    return caixaRepository.findCompativeis(comprimento, altura, largura, pesoMax);
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
