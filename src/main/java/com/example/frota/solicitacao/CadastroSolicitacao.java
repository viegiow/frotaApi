package com.example.frota.solicitacao;

import com.example.frota.caixa.Caixa;
import com.example.frota.produto.Produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroSolicitacao(
		double pedagio,
		double custoKm,
		double frete,
		@NotBlank
		String enderecoPartida,
		String enderecoDestino,
		Produto produto,
		Caixa caixa
		) {
}
