package com.example.frota.caminhao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CaminhaoRepository extends JpaRepository<Caminhao, Long>{
	public List<Caminhao> findByDisponivel(Boolean disponivel);

}
