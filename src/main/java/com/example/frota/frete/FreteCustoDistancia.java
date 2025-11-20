package com.example.frota.frete;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FreteCustoDistancia(
		@NotBlank(message="endereço de origem não pode ser nulo")
		String origem,
		@NotBlank(message="endereço de destino não pode ser nulo")
		String destino,
		@NotNull(message="id do produto não pode ser nulo")
		Long produtoId,
		@NotNull(message="id da caixa não pode ser nulo")
		Long caixaId
		) {

}
