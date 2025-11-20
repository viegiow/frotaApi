package com.example.frota.produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroProduto(
		@Min (value = 1, message="comprimento do produto é obrigatório e deve ser maior que 1")
		int comprimento,
		@Min (value = 1, message="altura do produto é obrigatória e deve ser maior que 1")
		int altura,
		@Min (value = 1, message="largura do produto é obrigatória e deve ser maior que 1")
		int largura,
		@Min (value = 1, message="peso do produto é obrigatório e deve ser maior que 1")
		double pesoProduto,
		@NotBlank(message="nome do produto é obrigatório")
		String nome) {

}
