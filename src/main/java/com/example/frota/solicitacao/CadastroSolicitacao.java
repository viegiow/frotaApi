package com.example.frota.solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroSolicitacao(
		@NotBlank(message="o endereço de origem não pode ser nulo")
		String enderecoPartida,
		@NotBlank(message="o endereço de destino não pode ser nulo")
		String enderecoDestino,
		@NotNull(message="o código do produto não pode ser nulo")
		Long produtoId,
		@NotNull(message="o código da caixa não pode ser nulo")
		Long caixaId
		) {
}
