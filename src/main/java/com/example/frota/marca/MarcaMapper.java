package com.example.frota.marca;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MarcaMapper {
	DadosAtualizacaoMarca toAtualizacaoDto(Marca marca);
	
	@Mapping(target = "id", ignore = true)
	Marca toEntityFromAtualizacao(DadosAtualizacaoMarca dto);
	
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(DadosAtualizacaoMarca dto, @MappingTarget Marca marca);
	

}
