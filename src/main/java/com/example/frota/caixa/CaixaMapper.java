package com.example.frota.caixa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CaixaMapper {
	AtualizacaoCaixa toAtualizacaoDto(Caixa caixa);
	
	@Mapping(target = "id", ignore = true)
	Caixa toEntityFromAtualizacao(AtualizacaoCaixa dto);
	
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(AtualizacaoCaixa dto, @MappingTarget Caixa caixa);
	
}