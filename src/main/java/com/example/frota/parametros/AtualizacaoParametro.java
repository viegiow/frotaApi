package com.example.frota.parametros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoParametro(
		Long id,
		@NotNull
		String nome,
		String valor) {

}