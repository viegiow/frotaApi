package com.example.frota.frete;

import java.net.URI;
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

        String normalizado = Normalizer.normalize(str, Normalizer.Form.NFD);

        String semAcentos = normalizado.replaceAll("\\p{M}", "");

        return semAcentos;
    }

    
    public Double calcularDistancia(String origem, String destino) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String origemEncoded  = removerAcentos(origem);
            String destinoEncoded = removerAcentos(destino);

            URI uri = UriComponentsBuilder
                    .fromUriString(BASE_URL) 
                    .queryParam("origins", origemEncoded)
                    .queryParam("destinations", destinoEncoded)
                    .queryParam("mode", "driving")
                    .queryParam("departure_time", "now")
                    .queryParam("key", API_KEY)
                    .encode(StandardCharsets.UTF_8)   
                    .build()
                    .toUri();

            String respostaJson = restTemplate.getForObject(uri, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(respostaJson);

            JsonNode rows = root.path("rows");
            if (!rows.isArray() || rows.isEmpty()) return null;

            JsonNode elements0 = rows.get(0).path("elements");
            if (!elements0.isArray() || elements0.isEmpty()) return null;

            String elementStatus = elements0.get(0).path("status").asText();
            if (!"OK".equals(elementStatus)) {
                return null;
            }

            JsonNode distanceValueNode = elements0.get(0).path("distance").path("value");
            if (distanceValueNode.isMissingNode() || !distanceValueNode.isNumber()) return null;

            double distanciaKm = distanceValueNode.asDouble() / 1000.0;

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
