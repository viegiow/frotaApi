package com.example.frota.frete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/frete")
@CrossOrigin("*")
public class FreteController {

	@Autowired
	private FreteService freteService;

	@GetMapping("/distancia")
    public Double obterDistancia(@RequestBody @Valid FreteDistancia dados) {
        return freteService.calcularDistancia(dados.origem(), dados.destino());
    }
	
	@GetMapping("")
	public Double obterCustoDistancia(@RequestBody @Valid FreteCustoDistancia dados) {
		// validar se produto cabe na caixa
		// validar se produto existe no sistema
		// validar se caixa existe no sistema
        return freteService.obterTotalFrete(dados);
    }
}