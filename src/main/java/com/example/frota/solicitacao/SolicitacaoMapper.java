package com.example.frota.solicitacao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.frota.caixa.Caixa;
import com.example.frota.produto.Produto;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {
	@Mapping(target = "produtoId", source = "produto.id")
	@Mapping(target = "caixaId", source = "caixa.id")
	AtualizacaoSolicitacao toAtualizacaoDto(Solicitacao solicitacao);

	// Converte DTO para Entity (para criação NOVA - ignora ID)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", source = "produtoId", qualifiedByName = "idToProduto")
	@Mapping(target = "caixa", source = "caixaId", qualifiedByName = "idToCaixa")
	Solicitacao toEntityFromAtualizacao(AtualizacaoSolicitacao dto);

	// Atualiza Entity existente com dados do DTO
	@Mapping(target = "id", ignore = true) // Não atualiza ID
	@Mapping(target = "produto", source = "produtoId", qualifiedByName = "idToProduto")
	@Mapping(target = "caixa", source = "caixaId", qualifiedByName = "idToCaixa")
	void updateEntityFromDto(AtualizacaoSolicitacao dto, @MappingTarget Solicitacao solicitacao);

	// Método para converter marcaId em objeto Marca
	@Named("idToProduto")
	default Produto idToProduto(Long produtoId) {
		if (produtoId == null) return null;
		Produto produto = new Produto();
		produto.setId(produtoId);
		return produto;
	}
	@Named("idToCaixa")
	default Caixa idToCaixa(Long caixaId) {
		if (caixaId == null) return null;
		Caixa caixa = new Caixa();
		caixa.setId(caixaId);
		return caixa;
	}

}
