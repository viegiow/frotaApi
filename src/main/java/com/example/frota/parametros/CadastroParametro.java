package com.example.frota.parametros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroParametro(
		@NotNull
		String nome,
		String valor) {

}