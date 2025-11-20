package com.example.frota.errors;

public class ProdutoIncompativelCaixa extends RuntimeException {
	
	public ProdutoIncompativelCaixa (String mensagem) {
        super(mensagem);
    }
}
