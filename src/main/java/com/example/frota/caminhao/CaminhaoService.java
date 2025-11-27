package com.example.frota.caminhao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.marca.Marca;
import com.example.frota.marca.MarcaService;

@Service
public class CaminhaoService {
	@Autowired
	private CaminhaoRepository caminhaoRepository;
	
	@Autowired
	private MarcaService marcaService;
	
	public Caminhao salvar(CadastroCaminhao dados) {
		Marca marca = marcaService.procurarPorId(dados.marcaId())
	            .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada com ID: " + dados.marcaId()));
        
		Caminhao novoCaminhao = new Caminhao(dados, marca);
		novoCaminhao.setMetragemCubica();
		return caminhaoRepository.save(novoCaminhao);
	}
	public List<Caminhao> procurarTodos(){
		return caminhaoRepository.findAll(Sort.by("modelo").ascending());
	}
	public void apagarPorId (Long id) {
		caminhaoRepository.deleteById(id);
	}
	
	public Optional<Caminhao> procurarPorId(Long id) {
	    return caminhaoRepository.findById(id);
	}

	public void atualizar(AtualizacaoCaminhao dados) {
		Marca marca = marcaService.procurarPorId(dados.marcaId())
	            .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada com ID: " + dados.marcaId()));
		Caminhao atualizarCaminhao = caminhaoRepository.findById(dados.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dados.id()));
		atualizarCaminhao.atualizarInformacoes(dados, marca);
		caminhaoRepository.save(atualizarCaminhao);
	}
	public void atualizarKmSaida(@Valid Long id) {
		Caminhao caminhao = caminhaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));
		caminhao.setKmSaida(caminhao.getKmChegada());
		caminhaoRepository.save(caminhao);
	}
	public void atualizarKmChegada(@Valid Double kmChegada, Long id) {
		Caminhao caminhao = caminhaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));
		Double novoKm = caminhao.getKmChegada() + kmChegada;
		caminhao.setKmChegada(novoKm);
		Double rodadoManutencao = novoKm - caminhao.getKmUltimaManutencao();
		Double rodadoPneu = novoKm - caminhao.getKmUltimaTrocaPneus();
		if (rodadoManutencao >= 10000) {
			System.out.println("manutenção de óleo, pastilha e filtro"); //precisa criar serviço da manutenção para criar registro da manutenção
			caminhao.setKmUltimaManutencao(novoKm);
		}
		if (rodadoPneu >= 70000) {
			System.out.println("troca do pneu"); //precisa criar serviço da manutenção para criar registro da manutenção
			caminhao.setKmUltimaTrocaPneus(novoKm);
		}
		caminhaoRepository.save(caminhao);
	}
}
