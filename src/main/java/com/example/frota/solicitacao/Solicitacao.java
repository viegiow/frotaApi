package com.example.frota.solicitacao;

import com.example.frota.caixa.Caixa;
import com.example.frota.produto.Produto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@ManyToOne
	@JoinColumn(name="caixa_id")
	private Caixa caixa;
	private Double frete;
	private Double pedagio;
	private String enderecoPartida;
	private String enderecoDestino;
	
	public Solicitacao(CadastroSolicitacao dados, Produto produto, Caixa caixa, Double frete, Double pedagio) {
		this.caixa = caixa;
		this.produto = produto;
		this.frete = frete;
		this.pedagio = pedagio;
		this.enderecoPartida = dados.enderecoPartida();
		this.enderecoDestino = dados.enderecoDestino();
	}
	public void atualizarSolicitacao(AtualizacaoSolicitacao dados, Produto produto, Caixa caixa, Double frete, Double pedagio) {
		this.caixa = caixa;
		this.produto = produto;
		this.frete = frete;
		this.pedagio = pedagio;
		this.enderecoPartida = dados.enderecoPartida();
		this.enderecoDestino = dados.enderecoDestino();
	}
}

