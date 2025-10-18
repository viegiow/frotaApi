package com.example.frota.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
