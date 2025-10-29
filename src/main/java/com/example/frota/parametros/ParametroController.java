package com.example.frota.parametros;

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
@RequestMapping("/parametro")
public class ParametroController {
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private ParametroMapper parametroMapper;
	
	@GetMapping
	public String carregaFormulario (Model model) {
		model.addAttribute("listaParametros", parametroService.procurarTodas());
		return "parametro/listagem";
	}

	@GetMapping("/formulario")
	public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
		AtualizacaoParametro dto;
		if (id != null) {
			//edição: Carrega dados existentes
			Parametro parametro = parametroService.procurarPorId(id)
					.orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado"));
				dto = parametroMapper.toAtualizacaoDto(parametro);
		} else {
			dto = new AtualizacaoParametro(id, "", "");
		}
		model.addAttribute("parametro", dto);
		return "parametro/formulario";
	}

	@GetMapping("/formulario/{id}")    
	public String carregaFormulario (@PathVariable("id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		AtualizacaoParametro dto;
		try {
			if(id != null) {
				Parametro parametro = parametroService.procurarPorId(id)
						.orElseThrow(() -> new EntityNotFoundException("Parametro não encontrado"));
				//mapear caminhão para AtualizacaoCaminhao
				dto = parametroMapper.toAtualizacaoDto(parametro);
				model.addAttribute("parametro", dto);
			}
			return "parametro/formulario";
		} catch (EntityNotFoundException e) {
			//resolver erros
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/parametro";
		}
	}
	
	@PostMapping("/salvar")
    public String salvar(@ModelAttribute("parametro") @Valid AtualizacaoParametro dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) throws Exception {
		if (result.hasErrors()) {
	        // Recarrega dados necessários para mostrar erros
	        return "parametro/formulario";
	    }
	    try {
			if(dto.id()==null) {
				throw new Exception("Cadastro de novo parâmetro bloqueado no momento. Contate a equipe de TI.");
			}
	        Parametro parametroSalvo = parametroService.salvarOuAtualizar(dto);
	        String mensagem = dto.id() != null 
	            ? "Parametro atualizado com sucesso!"
	            : "Parametro criado com sucesso!";
	        redirectAttributes.addFlashAttribute("message", mensagem);
	        return "redirect:/parametro";
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/parametro/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
	    }
	}
	
	

}
