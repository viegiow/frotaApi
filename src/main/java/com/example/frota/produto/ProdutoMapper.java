package com.example.frota.produto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
	AtualizacaoProduto toAtualizacaoDto(Produto produto);
	
	@Mapping(target = "id", ignore = true)
	Produto toEntityFromAtualizacao(AtualizacaoProduto dto);
	
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(AtualizacaoProduto dto, @MappingTarget Produto produto);

}
