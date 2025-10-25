package com.example.frota.frete;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.frota.caminhao.AtualizacaoCaminhao;
import com.example.frota.caminhao.Caminhao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

@Service
public class FreteService {

    private static final String API_KEY = "AIzaSyAP1IE7RV5OqTAGPNnw2NbI8jNesEnXT1Y";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
	
    public double calcularValorPorPeso(double peso) {
        final double custoPorPeso = 2.5;
        return peso * custoPorPeso;
    }

    public static String removerAcentos(String str) {
        if (str == null) return null;

        // Normaliza para decompor caracteres acentuados
        String normalizado = Normalizer.normalize(str, Normalizer.Form.NFD);

        // Remove acentos e sinais diacríticos
        String semAcentos = normalizado.replaceAll("\\p{M}", "");

        return semAcentos;
    }

    public Double calcularDistancia(String origem, String destino) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            String origemEncoded = removerAcentos(origem);
            String destinoEncoded = removerAcentos(destino);


            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("origins", origemEncoded)
                    .queryParam("destinations", destinoEncoded)
                    .queryParam("mode", "driving")
                    .queryParam("departure_time", "now")
                    .queryParam("key", API_KEY)
                    .toUriString();

            String respostaJson = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(respostaJson);

            // Caminho: rows[0].elements[0].distance.value (em metros)
            JsonNode distanceValueNode = root
                    .path("rows")
                    .get(0)
                    .path("elements")
                    .get(0)
                    .path("distance")
                    .path("value");

            if (distanceValueNode.isMissingNode()) {
                return null; // ou lançar exceção customizada
            }

            // Converte metros → quilômetros (ex: 96300 → 96.3)
            double distanciaKm = distanceValueNode.asDouble() / 1000.0;

            // Opcional: arredondar para 2 casas decimais
            return Math.round(distanciaKm * 100.0) / 100.0;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double calcularCustoPorDistancia(double distancia) {
        final double custoPorDistancia = 3;
        return distancia * custoPorDistancia;
    }
	
	
	
}
