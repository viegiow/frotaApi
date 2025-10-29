package com.example.frota.caixa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Caixa {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int comprimento;
	private int largura;
	private int altura;
	private String material;
	private int limitePeso;
	private Double pesoCubado;
	
	public void setPesoCubado(Double pesoCubado) {
		if (this.comprimento == 0 || this.largura == 0 || this.altura == 0) {this.pesoCubado = null;}
		this.pesoCubado = ((this.comprimento/100.0) * (this.largura/100.0) * (this.altura/100.0)) * 300;
	}
}
