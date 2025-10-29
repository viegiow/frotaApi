package com.example.frota.caixa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
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
	@Setter(AccessLevel.NONE) 
	private Double pesoCubado;
	
	 public void calcularPesoCubadoComFator(Double fatorCubagem) {
        if (fatorCubagem == null || comprimento == 0 || largura == 0 || altura == 0) {
            this.pesoCubado = null;
        } else {
            this.pesoCubado = ((comprimento/100.0) * (largura/100.0) * (altura/100.0)) * 300;
        }
    }


}
