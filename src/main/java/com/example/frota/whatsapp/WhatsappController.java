package com.example.frota.whatsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    @Autowired
    private WhatsappService whatsappService;

    @PostMapping("/enviar")
    public String enviar(@RequestParam String to) {
        whatsappService.enviarMensagem(to);
        return "Mensagem enviada!";
    }
}
