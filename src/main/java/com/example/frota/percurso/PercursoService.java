package com.example.frota.percurso;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoRepository;
import com.example.frota.caminhao.CaminhaoService;
import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.solicitacao.SolicitacaoService;

@Service
public class PercursoService {
    @Autowired
    private PercursoRepository percursoRepository;

    @Autowired
    private CaminhaoRepository caminhaoRepository;
    
    @Autowired
    private CaminhaoService caminhaoService;
    
    @Autowired
    private SolicitacaoService solicitacaoService;
    
    public void criarPercurso(CadastroPercurso dados) {
    	Caminhao cam = caminhaoService.procurarPorId(dados.caminhaoId())
    			.orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
    	Percurso percurso = new Percurso();
        percurso.setCaminhao(cam);
        percurso.setOrigem(dados.origem());
        percurso.setDestino(dados.destino());
        percurso.setStatus(StatusPercurso.AGUARDANDO_INICIO);
        Percurso percursoSalvo = percursoRepository.save(percurso);
        
    }

    // INICIAR UM PERCURSO
    public void iniciarPercurso(CadastroPercurso dados) {
        Caminhao cam = caminhaoRepository.findById(dados.caminhaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado"));

        // Verifica se já existe um percurso em andamento
        percursoRepository.findByCaminhaoIdAndStatus(cam.getId(), StatusPercurso.EM_ANDAMENTO)
                .ifPresent(p -> {
                    throw new IllegalStateException("Esse caminhão já está em um percurso");
                });

        Percurso percurso = new Percurso();
        percurso.setCaminhao(cam);
        percurso.setOrigem(dados.origem());
        percurso.setDestino(dados.destino());
        percurso.setDataSaida(LocalDateTime.now());
        percurso.setKmSaida(cam.getKmChegada());
        percurso.setStatus(StatusPercurso.EM_ANDAMENTO);

        percursoRepository.save(percurso);
        
        caminhaoService.atualizarKmSaida(dados.caminhaoId());
        solicitacaoService.aCaminho(null);
    }
    
    // FINALIZAR PERCURSO
    public Percurso finalizarPercurso(FinalizarPercurso dados) {

        Percurso percurso = percursoRepository.findById(dados.percursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Percurso não encontrado"));
        Caminhao cam = percurso.getCaminhao();
        percurso.setDataChegada(LocalDateTime.now());
        percurso.setKmChegada(cam.getKmChegada());
        percurso.setStatus(StatusPercurso.FINALIZADO);
        return percursoRepository.save(percurso);
    }
}
