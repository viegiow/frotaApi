package com.example.frota.whatsapp;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class WhatsappService {
	private String accountSid = "ACdad734bd0b337648bb1316a194920510";
	private String authToken = "820e132996755280e24d592d536f54c5";
	private String from = "whatsapp:+14155238886";

	public void enviarMensagem(String to) {
		Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(from),
                "Entrega a caminho!"
        ).create();
	}
}
