package com.example.frota.manutencao;

import java.time.LocalDate;

import com.example.frota.caminhao.Caminhao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of = "id")
public class Manutencao {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private Double kmRealizado;
    @Enumerated(EnumType.STRING)
    private TipoManutencao tipo;
    @ManyToOne
    private Caminhao caminhao;

    public Manutencao (LocalDate data, Double kmChegada, TipoManutencao tipo, Caminhao caminhao) {
    	this.data = data;
    	this.kmRealizado = kmChegada;
    	this.tipo = tipo;
    	this.caminhao = caminhao;
    }
}
