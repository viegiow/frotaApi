package com.example.frota.pagamento;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.frota.solicitacao.Solicitacao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class Pagamento {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String tipo;
	private LocalDateTime dataPagamento;
	private String numeroCartao;
	private String codigoSeguranca;
	private LocalDate dataVencimentoCartao;
	private Boolean aprovado;
	@ManyToOne
	@JoinColumn(name="solicitacao_id")
	private Solicitacao solicitacao;
//	@ManyToOne
//	private Entrega entrega;
	
	public Pagamento(CadastroPagamento dados, Solicitacao solicitacao) {
		this.tipo = dados.tipo();
		this.solicitacao=solicitacao;
		this.numeroCartao = dados.numeroCartao();
		this.codigoSeguranca = dados.codigoSeguranca();
		this.dataVencimentoCartao = dados.dataVencimentoCartao();
		//this.aprovado = dados.aprovado();
	}

	public void atualizarPagamento(AtualizacaoPagamento dados) {
		
	}
}
