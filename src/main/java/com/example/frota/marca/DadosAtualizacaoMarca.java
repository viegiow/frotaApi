package com.example.frota.marca;

import jakarta.validation.constraints.NotBlank;
public record DadosAtualizacaoMarca(
		Long id,
		@NotBlank
		String nome,
		String pais) {
}
