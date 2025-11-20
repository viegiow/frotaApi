package com.example.frota.parametros;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoParametro(
		@NotNull(message="id não pode ser nulo")
		Long id,
		@NotNull(message="valor não pode ser nulo")
		@Min(value=1, message="valor deve ser maior ou igual a 1")
		String valor) {

}