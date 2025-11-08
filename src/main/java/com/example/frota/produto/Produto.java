package com.example.frota.produto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
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
public class Produto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private int comprimento;
	private int largura;
	private int altura;
	private double pesoProduto;
	
	public Produto(CadastroProduto dados) {
		this.nome = dados.nome();
		this.comprimento = dados.comprimento();
		this.largura = dados.largura();
		this.altura = dados.altura();
		this.pesoProduto = dados.pesoProduto();
	}

	public void atualizar(@Valid AtualizacaoProduto dados) {
		if (dados.id() != null) {
			this.nome = dados.nome();
			this.comprimento = dados.comprimento();
			this.largura = dados.largura();
			this.altura = dados.altura();
			this.pesoProduto = dados.pesoProduto();
		}
	}
}
