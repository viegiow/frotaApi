package com.example.frota.solicitacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroSolicitacao(
		@NotBlank(message="o endereço de origem não pode ser nulo")
		String enderecoPartida,
		@NotBlank(message="o endereço de destino não pode ser nulo")
		String enderecoDestino,
		@NotNull(message="o código do produto não pode ser nulo")
		Long caixaId,
		@NotNull(message="horário de coleta não pode ser nulo")
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		String horaColeta,
		@NotBlank(message="o telefone para contato não pode ser nulo")
		String telefoneContato,
		@Min (value = 1, message="comprimento do produto é obrigatório e deve ser maior que 1")
		int comprimentoProduto,
		@Min (value = 1, message="altura do produto é obrigatória e deve ser maior que 1")
		int alturaProduto,
		@Min (value = 1, message="largura do produto é obrigatória e deve ser maior que 1")
		int larguraProduto,
		@Min (value = 1, message="peso do produto é obrigatório e deve ser maior que 1")
		double pesoProduto,
		@NotBlank(message="nome do produto é obrigatório")
		String nomeProduto
		) 
{
	
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
