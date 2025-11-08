package com.example.frota.solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoSolicitacao(
		Long id,
		Double pedagio,
		Double custoKm,
		Double frete,
		@NotBlank
		String enderecoPartida,
		String enderecoDestino,
		Long produtoId,
		Long caixaId) {

}
