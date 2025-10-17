package com.example.frota.caixa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroCaixa(
		@NotBlank
		String material,
		@NotNull
		int comprimento,
		int largura,
		int altura,
		int limitePeso) {

}
