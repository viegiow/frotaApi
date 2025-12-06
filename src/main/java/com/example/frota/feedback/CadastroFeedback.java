package com.example.frota.feedback;

import jakarta.validation.constraints.Min;

public record CadastroFeedback(
		@Min(value=0, message="a nota deve ser igual ou maior que 0")
		int nota,
		String tipo,
		Long solicitacao_id
		) {
}
