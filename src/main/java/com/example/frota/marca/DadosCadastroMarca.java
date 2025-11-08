package com.example.frota.marca;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroMarca(	
		@NotBlank(message="nome da marca é obrigatório")
		String nome,
		@NotBlank(message="país da marca é obrigatório")
		String pais) {
		

}
