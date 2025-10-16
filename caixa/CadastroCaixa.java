package com.example.frota.caixa;

import jakarta.validation.constraints.NotBlank;

public record CadastroCaixa(
		@NotBlank
		int comprimento,
		int largura,
		int altura,
		String material,
		int limitePeso) {

}
