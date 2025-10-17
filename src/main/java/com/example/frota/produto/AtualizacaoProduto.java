package com.example.frota.produto;

import jakarta.validation.constraints.NotNull;

public record AtualizacaoProduto(
		@NotNull
		int comprimento,
		int largura,
		int altura,
		double pesoProduto) {

}
