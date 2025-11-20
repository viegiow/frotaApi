package com.example.frota.caixa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.parametros.Parametro;
import com.example.frota.parametros.ParametroService;
import com.example.frota.produto.Produto;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CaixaService {
	@Autowired
	private CaixaRepository caixaRepository;

	@Autowired
	private ParametroService parametroService;
	
	public List<Caixa> procurarCompativeis(Produto produto) {
	    return caixaRepository.findCompativeis(produto.getComprimento(), produto.getAltura(), produto.getLargura(), produto.getPesoProduto());
	}

	public List<Caixa> procurarTodas() {
		return caixaRepository.findAll();
	}
	
	public Optional<Caixa> procurarPorId(Long id) {
		return caixaRepository.findById(id);
	}
	
	public void apagarPorId (Long id) {
		caixaRepository.deleteById(id);
	}

	public void salvar(CadastroCaixa dados) {
		Parametro parametro = parametroService.procurarPorNome("FATOR_CUBAGEM")
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro de custo por peso não encontrado"));;
        Double fatorCubagem = Double.parseDouble(parametro.getValor());
		Caixa novaCaixa = new Caixa(dados, fatorCubagem);
		caixaRepository.save(novaCaixa);
	}

	public void atualizar(AtualizacaoCaixa dados) {
		Parametro parametro = parametroService.procurarPorNome("FATOR_CUBAGEM")
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro de custo por peso não encontrado"));
        Double fatorCubagem = Double.parseDouble(parametro.getValor());
		Caixa caixaExistente = caixaRepository.findById(dados.id())
				.orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada"));
		caixaExistente.CaixaAtualizar(dados, fatorCubagem);
		caixaRepository.save(caixaExistente);
	}

}
