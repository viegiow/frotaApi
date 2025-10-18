package com.example.frota.solicitacao;

import com.example.frota.caixa.Caixa;
import com.example.frota.produto.Produto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Solicitacao {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name="produto_id")
	private Produto produto;
	@OneToOne
	@JoinColumn(name="caixa_id")
	private Caixa caixa;
	private double frete;
	private double pedagio;
	private double custoKm;
	private String enderecoPartida;
	private String enderecoDestino;
}

