package com.example.frota.percurso;

import jakarta.validation.constraints.NotBlank;

public record CadastroPercurso(
		Long caminhaoId,
		@NotBlank(message="o endereço de partida é obrigatório")
		String origem,
		@NotBlank(message="o endereço de destino é obrigatório")
		String destino
	) {
}
