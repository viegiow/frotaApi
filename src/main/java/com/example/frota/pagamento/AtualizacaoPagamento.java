package com.example.frota.pagamento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoPagamento(
		@NotNull(message="id não deve ser nulo")
		Long id,
		@Min(value=0, message="a nota deve ser igual ou maior que 0")
		int nota		
		) {
	

}
