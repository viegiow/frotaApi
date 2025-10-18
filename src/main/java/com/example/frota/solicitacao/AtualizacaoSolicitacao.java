package com.example.frota.solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoSolicitacao(
		Long id,
		double pedagio,
		double custoKm,
		double frete,
		@NotBlank
		String enderecoPartida,
		String enderecoDestino,
		Long produtoId,
		Long caixaId) {

}
