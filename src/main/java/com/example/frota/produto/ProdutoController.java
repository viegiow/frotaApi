package com.example.frota.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoMapper produtoMapper;
	
	@GetMapping
	public String carregaFormulario (Model model) {
		model.addAttribute("listaProdutos", produtoService.procurarTodas());
		return "produto/listagem";
	}

	@GetMapping("/formulario")
	public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
		AtualizacaoProduto dto;
		if (id != null) {
			//edição: Carrega dados existentes
			Produto produto = produtoService.procurarPorId(id)
					.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrada"));
				dto = produtoMapper.toAtualizacaoDto(produto);
		} else {
			dto = new AtualizacaoProduto(id, 0, 0, 0, 0, null);
		}
		model.addAttribute("produto", dto);
		return "produto/formulario";
	}

	@GetMapping("/formulario/{id}")    
	public String carregaFormulario (@PathVariable("id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		AtualizacaoProduto dto;
		try {
			if(id != null) {
				Produto produto = produtoService.procurarPorId(id)
						.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
				//mapear caminhão para AtualizacaoCaminhao
				dto = produtoMapper.toAtualizacaoDto(produto);
				model.addAttribute("produto", dto);
			}
			return "produto/formulario";
		} catch (EntityNotFoundException e) {
			//resolver erros
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/produto";
		}
	}
	
	@PostMapping("/salvar")
    public String salvar(@ModelAttribute("produto") @Valid AtualizacaoProduto dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) {
		if (result.hasErrors()) {
	        // Recarrega dados necessários para mostrar erros
	        return "produto/formulario";
	    }
	    try {
	        Produto produtoSalvo = produtoService.salvarOuAtualizar(dto);
	        String mensagem = dto.id() != null 
	            ? "Produto atualizado com sucesso!"
	            : "Produto criado com sucesso!";
	        redirectAttributes.addFlashAttribute("message", mensagem);
	        return "redirect:/produto";
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/produto/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
	    }
	}
	
	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteTutorial(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			produtoService.apagarPorId(id);
			redirectAttributes.addFlashAttribute("message", "O produto foi apagado!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/produto";
	}

}
