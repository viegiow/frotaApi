package com.example.frota.caminhao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CaminhaoAtualizaKm(
		@NotNull(message="id não pode ser nulo")
		Long id,
		@NotNull(message="valor do km não pode ser nulo")
		@Min(value=0, message="valor do km deve ser maior ou igual a 0")
		Double km) {

}
