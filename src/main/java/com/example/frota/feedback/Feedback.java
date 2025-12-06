package com.example.frota.feedback;

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
public class Feedback {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int nota;
	private String tipo;
	@ManyToOne
	@JoinColumn(name="solicitacao_id")
	private Solicitacao solicitacao;
//	@ManyToOne
//	private Entrega entrega;
	
	public Feedback(CadastroFeedback dados, Solicitacao solicitacao) {
		this.nota = dados.nota();
		this.tipo = dados.tipo();
		this.solicitacao=solicitacao;
	}

	public void atualizarFeedback(AtualizacaoFeedback dados) {
		
	}
}
