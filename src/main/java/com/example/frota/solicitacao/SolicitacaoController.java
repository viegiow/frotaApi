package com.example.frota.solicitacao;

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

import com.example.frota.caixa.CaixaService;
import com.example.frota.produto.ProdutoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/solicitacao")
public class SolicitacaoController {
	@Autowired
	private SolicitacaoService solicitacaoService;

	@Autowired
	private SolicitacaoMapper solicitacaoMapper;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private CaixaService caixaService;

	@GetMapping                 
	public String carregaPaginaFormulario ( Model model){ 
		model.addAttribute("listaSolicitacoes", solicitacaoService.procurarTodos());
	    return "solicitacao/listagem";              
	} 
	////////////////////////
	//Novo GetMapping com DTO e Mapper
	@GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
		AtualizacaoSolicitacao dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Solicitacao solicitacao = solicitacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));
            dto = solicitacaoMapper.toAtualizacaoDto(solicitacao);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoSolicitacao(null, 0.0, 0.0, 0.0, null, null, null, null);
        }
        model.addAttribute("solicitacao", dto);
        model.addAttribute("produtos", produtoService.procurarTodas());
        model.addAttribute("caixas", caixaService.procurarTodas());
        return "solicitacao/formulario";
    }
	
	@GetMapping ("/formulario/{id}")    
	public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		AtualizacaoSolicitacao dto;
		try {
			if(id != null) {
				Solicitacao solicitacao = solicitacaoService.procurarPorId(id)
						.orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));
				model.addAttribute("produtos", produtoService.procurarTodas());
		        model.addAttribute("caixas", caixaService.procurarTodas());
				//mapear caminhão para AtualizacaoCaminhao
				dto = solicitacaoMapper.toAtualizacaoDto(solicitacao);
				model.addAttribute("solicitacao", dto);
			}
			return "solicitacao/formulario";
		} catch (EntityNotFoundException e) {
			//resolver erros
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/solicitacao";
		}
	}
	

	@PostMapping("/salvar")
    public String salvar(@ModelAttribute("solicitacao") @Valid AtualizacaoSolicitacao dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) {
		if (result.hasErrors()) {
	        // Recarrega dados necessários para mostrar erros
			System.out.println(result);
			model.addAttribute("solicitacao", dto);
			model.addAttribute("produtos", produtoService.procurarTodas());
	        model.addAttribute("caixas", caixaService.procurarTodas());
	        return "solicitacao/formulario";
	    }
	    try {
	    	Solicitacao solicitacaoSalva = solicitacaoService.salvarOuAtualizar(dto);
	        String mensagem = dto.id() != null 
	            ? "Solicitação atualizada com sucesso!"
	            : "Solicitação criada com sucesso!";
	        redirectAttributes.addFlashAttribute("message", mensagem);
	        return "redirect:/solicitacao";
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/solicitacao/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
	    }
	}
	
//	@PostMapping("/calcularFrete")
//	public <Optional> String calcularFrete(@ModelAttribute("solicitacao") @Valid AtualizacaoSolicitacao dto,
//            BindingResult result,
//            RedirectAttributes redirectAttributes,
//            Model model) {
//		if (result.hasErrors()) {
//	        // Recarrega dados necessários para mostrar erros
//			System.out.println(result);
//			model.addAttribute("solicitacao", dto);
//			model.addAttribute("produtos", produtoService.procurarTodas());
//	        model.addAttribute("caixas", caixaService.procurarTodas());
//	        return "solicitacao/formulario";
//	    }
//	    try {
//	        model.addAttribute("produtos", produtoService.procurarTodas());
//	        model.addAttribute("caixas", caixaService.procurarTodas());
//	        model.addAttribute("message", "Frete calculado com sucesso!");
//	    	model.addAttribute("solicitacao", dto);
//	        String mensagem = dto.id() != null 
//	            ? "Frete atualizado com sucesso!"
//	            : "Frete calculado com sucesso!";
//	        redirectAttributes.addFlashAttribute("message", mensagem);
//	        return "solicitacao/formulario";
//	    } catch (EntityNotFoundException e) {
//	        redirectAttributes.addFlashAttribute("error", e.getMessage());
//	        return "redirect:/solicitacao/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
//	    }
//	}
	
	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteTutorial(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			solicitacaoService.apagarPorId(id);
			redirectAttributes.addFlashAttribute("message", "A solicitação foi apagada!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/caminhao";
	}
}
