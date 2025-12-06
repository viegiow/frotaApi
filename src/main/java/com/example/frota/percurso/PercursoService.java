package com.example.frota.percurso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoRepository;
import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.frete.FreteService;
import com.example.frota.rota.RouteOptimizationRequest;
import com.example.frota.rota.RouteOptimizationRequest.Coordinate;
import com.example.frota.rota.RouteOptimizationResponse.OrderedWaypoint;
import com.example.frota.rota.RouteOptimizationService;
import com.example.frota.solicitacao.Solicitacao;
import com.example.frota.solicitacao.SolicitacaoRepository;
import com.example.frota.solicitacao.SolicitacaoService;

import jakarta.transaction.Transactional;

@Service
public class PercursoService {
	@Autowired
	private PercursoRepository percursoRepository;

	@Autowired
	private CaminhaoRepository caminhaoRepository;
	
	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private FreteService freteService;
	
	@Autowired
	private RouteOptimizationService routeService;
	
	public Percurso procurarPercursoPorId(Long percursoId) {
		Percurso percurso = percursoRepository.findById(percursoId)
				.orElseThrow(() -> new ResourceNotFoundException("Percurso não encontrado"));
		return percurso;
	}
	
	public void criarPercurso(Caminhao caminhao, List<Solicitacao> solicitacoes) {
		try {
			Coordinate saida = new Coordinate(-23.0, -46.5);
			List<Coordinate> solicitacoesCoords = new ArrayList<Coordinate>();
			for (Solicitacao solicitacao : solicitacoes) {
				double[] solicitacaoCoords = freteService.obterCoordenadas(solicitacao.getEnderecoDestino());
				Coordinate destinoSolicitacao = new Coordinate(solicitacaoCoords[0], solicitacaoCoords[1]);
				solicitacoesCoords.add(destinoSolicitacao);
			}
			RouteOptimizationRequest reqPercursos = new RouteOptimizationRequest(saida, solicitacoesCoords);
			List<OrderedWaypoint> rotaOtimizada = routeService.optimize(reqPercursos).getWaypointsOrdered();
			Optional<OrderedWaypoint> ultimoDestino = rotaOtimizada.stream().reduce((a, b) -> b);

			Percurso percurso = new Percurso();
			percurso.setCaminhao(caminhao);
			percurso.setStatus(StatusPercurso.AGUARDANDO_INICIO);
			String enderecoDestino = freteService.obterEndereco(ultimoDestino.get().getLat(), ultimoDestino.get().getLng());
			String enderecoSaida = freteService.obterEndereco(saida.getLat(), saida.getLng());
			percurso.setOrigem(enderecoSaida);
			percurso.setDestino(enderecoDestino);
			Percurso percursoSalvo = percursoRepository.save(percurso);
			for (Solicitacao solicitacao : solicitacoes) {
				solicitacao.setPercurso(percursoSalvo);
				solicitacaoRepository.save(solicitacao);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// INICIAR UM PERCURSO
	@Transactional
	public void iniciarPercurso(Long id) {
		Percurso percurso = percursoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Percurso não encontrado"));
		percurso.setDataSaida(LocalDateTime.now());
		percurso.setKmSaida(percurso.getCaminhao().getKmChegada());
		percurso.setStatus(StatusPercurso.EM_ANDAMENTO);

		percursoRepository.save(percurso);

		percurso.getCaminhao().setKmSaida(percurso.getCaminhao().getKmChegada());
		caminhaoRepository.save(percurso.getCaminhao());
		List<Solicitacao> solicitacoesPercurso = solicitacaoService.procurarPorPercurso(percurso.getId());
		for (Solicitacao solicitacao : solicitacoesPercurso) {
			solicitacaoService.aCaminho(solicitacao.getId());
		}
		
	}

	// FINALIZAR PERCURSO
	public Percurso finalizarPercurso(FinalizarPercurso dados) {
		Percurso percurso = percursoRepository.findById(dados.percursoId())
				.orElseThrow(() -> new ResourceNotFoundException("Percurso não encontrado"));
		percurso.setDataChegada(LocalDateTime.now());
		percurso.setKmChegada(percurso.getCaminhao().getKmChegada());
		percurso.setStatus(StatusPercurso.FINALIZADO);
		percurso.getCaminhao().setDisponivel(true);
		return percursoRepository.save(percurso);
	}

	
}
