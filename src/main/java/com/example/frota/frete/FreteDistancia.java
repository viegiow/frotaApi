package com.example.frota.frete;

import jakarta.validation.constraints.NotBlank;

public record FreteDistancia(
		@NotBlank(message="endereço de destino não pode ser vazio")
		String destino,
		@NotBlank(message="endereço de origem não pode ser vazio")
		String origem
		) {
	

}
