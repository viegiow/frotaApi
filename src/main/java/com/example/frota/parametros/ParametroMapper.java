package com.example.frota.parametros;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParametroMapper {
	AtualizacaoParametro toAtualizacaoDto(Parametro parametro);
	
	@Mapping(target = "id", ignore = true)
	Parametro toEntityFromAtualizacao(AtualizacaoParametro dto);
	
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(AtualizacaoParametro dto, @MappingTarget Parametro parametro);

}