package com.example.frota.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroProduto(
		@NotNull
		int comprimento,
		int altura,
		int largura,
		double pesoProduto,
		@NotBlank
		String nome) {

}
