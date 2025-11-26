package com.example.frota.feedback;

import com.example.frota.solicitacao.Solicitacao;

import jakarta.persistence.Entity;
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
@EqualsAndHashCode(of ="id")
public class Feedback {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int nota;
	@ManyToOne
	private Solicitacao solicitacao;
//	@ManyToOne
//	private Entrega entrega;
	
	public Feedback(CadastroFeedback dados, String tipo) {
		this.nota = dados.nota();
	}

	public void atualizarFeedback(AtualizacaoFeedback dados) {
		
	}
}
