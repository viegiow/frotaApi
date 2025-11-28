package com.example.frota.errors;

public class StatusErrado extends RuntimeException{
	public StatusErrado(String mensagem) {
		super(mensagem);
	}
}
