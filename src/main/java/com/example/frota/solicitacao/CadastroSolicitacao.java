package com.example.frota.solicitacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

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
		Long caixaId,
		@NotNull(message="horário de coleta não pode ser nulo")
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		String horaColeta
		) {
	
	public CadastroSolicitacao {
        validarFormato(horaColeta);
    }

    private void validarFormato(String valor) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime.parse(valor, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "O campo horaColeta deve estar no formato: yyyy-MM-dd'T'HH:mm"
            );
        }
    }
}
