package com.example.frota.parametros;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Parametro {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "O nome é obrigatório e não pode estar vazio")
	@Column(nullable = false, unique = true, updatable = false)
	private String nome;
	private String valor;

	// hook executado antes de INSERT e UPDATE
    @PrePersist
    @PreUpdate
    private void toUpperCaseFields() {
        if (nome != null) nome = nome.toUpperCase();
    }
}