package com.example.frota.feedback;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoFeedback(
		@NotNull(message="id não deve ser nulo")
		Long id,
		@Min(value=0, message="a nota deve ser igual ou maior que 0")
		int nota		
		) {
	

}
