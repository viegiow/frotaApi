package com.example.frota.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoProduto(
		Long id,
		@NotNull
		int comprimento,
		int largura,
		int altura,
		double pesoProduto,
		@NotBlank
		String nome) {

}
