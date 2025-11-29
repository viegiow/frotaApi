package com.example.frota.caminhao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.manutencao.Manutencao;
import com.example.frota.manutencao.TipoManutencao;
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

	public List<Caminhao> procurarTodos() {
		return caminhaoRepository.findAll(Sort.by("modelo").ascending());
	}

	public void apagarPorId(Long id) {
		caminhaoRepository.deleteById(id);
	}

	public Optional<Caminhao> procurarPorId(Long id) {
		return caminhaoRepository.findById(id);
	}

	private void verificarManutencoes(Caminhao caminhao) {
		Double kmRodadoDesdeUltimaManut = caminhao.getKmAtual() - caminhao.getKmUltimaManutencao();
		Double kmRodadoDesdeUltimaTrocaPneu = caminhao.getKmAtual() - caminhao.getKmUltimaTrocaPneus();

		// Manutenção geral a cada 10.000 km
		if (kmRodadoDesdeUltimaManut >= 10000) {
			caminhao.setManutencaoPendente(true);
		} else {
			caminhao.setManutencaoPendente(false);
		}

		// Troca de pneus a cada 70.000 km
		if (kmRodadoDesdeUltimaTrocaPneu >= 70000) {
			caminhao.setTrocaPneusPendente(true);
		} else {
			caminhao.setTrocaPneusPendente(false);
		}
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

	public void atualizarKmChegada(Double kmChegada, Long id) {
		Caminhao caminhao = caminhaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado."));

		// Atualiza km total do caminhão
		Double novoKm = caminhao.getKmChegada() + kmChegada;
		caminhao.setKmChegada(novoKm);

		// --- ATUALIZA O KM ATUAL ---
		caminhao.setKmAtual(novoKm);

		// --- CHAMA A REGAR DE NEGÓCIO ---
		verificarManutencoes(caminhao);

		Double rodadoManutencao = novoKm - caminhao.getKmUltimaManutencao();
		Double rodadoPneu = novoKm - caminhao.getKmUltimaTrocaPneus();

		if (rodadoPneu >= 70000.0) {
			caminhao.setKmUltimaTrocaPneus(novoKm);
			registrarManutencao(caminhao, kmChegada, TipoManutencao.PNEUS);
		}
		if (rodadoManutencao >= 10000.0) {
			caminhao.setKmUltimaManutencao(novoKm);
			registrarManutencao(caminhao, kmChegada, TipoManutencao.OLEO_FILTRO_PASTILHA);
		}

		caminhaoRepository.save(caminhao);
	}

	private void registrarManutencao(Caminhao caminhao, Double kmChegada, TipoManutencao tipo) {
		Manutencao m = new Manutencao(LocalDate.now(), kmChegada, tipo, caminhao);

		caminhao.getManutencoes().add(m);
		caminhaoRepository.save(caminhao);
	}

}
