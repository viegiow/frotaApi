package com.example.frota.caminhao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroCaminhao(
		@NotBlank
		String modelo,
		String placa,
		Long marcaId,
		double cargaMaxima,
		@NotNull
		int ano,
		int comprimento,
		int largura,
		int altura) {

}

