package com.example.frota.errors;

public class EntregaJaRealizada extends RuntimeException {
	public EntregaJaRealizada(String message) {
		super(message);
	}
}
