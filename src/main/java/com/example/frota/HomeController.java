package com.example.frota;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inicio")
public class HomeController {
	
	@GetMapping
	public String carregaInicio (Model model) {
		return "inicio/inicio";
	}

}
