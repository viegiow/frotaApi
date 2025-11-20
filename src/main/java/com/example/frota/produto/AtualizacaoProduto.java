package com.example.frota.produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoProduto(
		@NotNull(message="id do produto é obrigatório")
		Long id,
		@Min(value=1, message="comprimento é obrigatório e deve ser maior que 0")
		int comprimento,
		@Min(value=1, message="largura é obrigatória e deve ser maior que 0")
		int largura,
		@Min(value=1, message="altura é obrigatória e deve ser maior que 0")
		int altura,
		@Min(value=1, message="peso é obrigatório e deve ser maior que 0")
		double pesoProduto,
		@NotBlank(message="nome é obrigatório")
		String nome) {

}
