package com.example.frota.caminhao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroCaminhao(
		@NotBlank(message="modelo do caminhão é obrigatório")
		String modelo,
		@NotBlank(message="placa do caminhão é obrigatória")
		String placa,
		@NotNull(message="id da marca do caminhão é obrigatória")
		Long marcaId,
		@Min(value = 1, message="carga maxima do caminhão é obrigatória e deve ser maior que 1")
		double cargaMaxima,
		@Min(value = 1900, message="ano do caminhão é obrigatório e deve ser maior que 1900")
		int ano,
		@Min(value = 1, message="comprimento do caminhão é obrigatório e deve ser maior que 1")
		int comprimento,
		@Min(value = 1, message="largura do caminhão é obrigatória e deve ser maior que 1")
		int largura,
		@Min(value = 1, message="altura do caminhão é obrigatória e deve ser maior que 1")
		int altura) {

}

