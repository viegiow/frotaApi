package com.example.frota.caixa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CadastroCaixa(
		@NotBlank(message="material é obrigatório")
		String material,
		@Min(value=1, message="comprimento é obrigatório e deve ser maior que 0")
		int comprimento,
		@Min(value=1, message="largura é obrigatória e deve ser maior que 0")
		int largura,
		@Min(value=1, message="altura é obrigatória e deve ser maior que 0")
		int altura,
		@Min(value=1, message="peso é obrigatório e deve ser maior que 0")
		int limitePeso) {

}
