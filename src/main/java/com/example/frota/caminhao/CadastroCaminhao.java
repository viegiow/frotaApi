package com.example.frota.caminhao;

import com.example.frota.marca.Marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroCaminhao(
		@NotBlank
		String modelo,
		String placa,
		Marca marca,
		double cargaMaxima,
		@NotNull
		int ano,
		int comprimento,
		int largura,
		int altura) {

}

