package com.example.frota.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record DadosAtualizacaoMarca(
		
		@NotNull(message="o id da marca é obrigatório")
		Long id,
		@NotBlank(message="o nome da marca é obrigatório")
		String nome,
		@NotBlank(message="o país da marca é obrigatório")
		String pais) {
}
