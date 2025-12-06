package com.example.frota.pagamento;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.frota.solicitacao.Solicitacao;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;

public record CadastroPagamento(
		 String tipo,
		 String numeroCartao,
		 String codigoSeguranca,
		 LocalDate dataVencimentoCartao,
		 Long solicitacao_id
		) {
}
