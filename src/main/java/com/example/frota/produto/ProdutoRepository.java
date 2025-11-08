package com.example.frota.produto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	@Query("SELECT p FROM Produto p WHERE p.id NOT IN (SELECT s.produto.id FROM Solicitacao s)")
	List<Produto> findProdutosSemSolicitacao();


}
