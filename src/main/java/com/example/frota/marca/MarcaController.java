package com.example.frota.marca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frota.caminhao.AtualizacaoCaminhao;
import com.example.frota.caminhao.Caminhao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/marca")
public class MarcaController {

	@Autowired
	private MarcaService marcaService;
	
	@Autowired
	private MarcaMapper marcaMapper;

	@GetMapping              
	public String carregaPaginaListagem(Model model){ 
		model.addAttribute("listaMarcas",marcaService.procurarTodos() );
		return "marca/listagem";              
	} 
	@GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
		DadosAtualizacaoMarca dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Marca marca = marcaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado"));
            dto = marcaMapper.toAtualizacaoDto(marca);
        } else {
            // criação: DTO vazio
            dto = new DadosAtualizacaoMarca(null, "", "");
        }
        model.addAttribute("marca", dto);
        return "marca/formulario";
    }
	
	@GetMapping ("/formulario/{id}")    
	public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		DadosAtualizacaoMarca dto;
		try {
			if(id != null) {
				Marca marca = marcaService.procurarPorId(id)
						.orElseThrow(() -> new EntityNotFoundException("Caminhao não encontrado"));
				//mapear caminhão para AtualizacaoCaminhao
				dto = marcaMapper.toAtualizacaoDto(marca);
				model.addAttribute("marca", dto);
			}
			return "marca/formulario";
		} catch (EntityNotFoundException e) {
			//resolver erros
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/marca";
		}
	}
	

	@PostMapping("/salvar")
    public String salvar(@ModelAttribute("caminhao") @Valid DadosAtualizacaoMarca dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) {
		if (result.hasErrors()) {
	        // Recarrega dados necessários para mostrar erros
	        return "marca/formulario";
	    }
	    try {
	        Marca marcaSalva = marcaService.salvarOuAtualizar(dto);
	        String mensagem = dto.id() != null 
	            ? "Marca '" + marcaSalva.getNome() + "' atualizada com sucesso!"
	            : "Marca '" + marcaSalva.getNome() + "' criada com sucesso!";
	        redirectAttributes.addFlashAttribute("message", mensagem);
	        return "redirect:/marca";
	    } catch (EntityNotFoundException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/marca/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
	    }
	}
	
	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteTutorial(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			marcaService.apagarPorId(id);
			redirectAttributes.addFlashAttribute("message", "A marca " + id + " foi apagada!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/marca";
	}
}
