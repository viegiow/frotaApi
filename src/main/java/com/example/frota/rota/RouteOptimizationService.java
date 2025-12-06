package com.example.frota.rota;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteOptimizationService {
    private String apiKey = "mcrxZoUUlLNigSmNNbAZQ1zx0xJ67_wZK23Vhp6SGck";

    private final RestTemplate restTemplate = new RestTemplate();

    public RouteOptimizationResponse optimize(RouteOptimizationRequest request) {
        // Monta os waypoints (origem + destinos)
        StringBuilder url = new StringBuilder(
                "https://wps.hereapi.com/v8/findsequence2?"
        );

        // Origem
        url.append("start=")
           .append(request.getOrigem().getLat())
           .append(",")
           .append(request.getOrigem().getLng())
           .append("&");

        // Destinos
        int i = 1;
        for (RouteOptimizationRequest.Coordinate d : request.getDestinos()) {
            url.append("destination")
               .append(i++)
               .append("=")
               .append(d.getLat())
               .append(",")
               .append(d.getLng())
               .append("&");
        }
        
        url.append("mode=fastest;truck&");

        // HERE API KEY
        url.append("apikey=").append(apiKey);

        // Chama a API
        Map response = restTemplate.getForObject(url.toString(), Map.class);

        // results é uma lista
        List<Map<String, Object>> results = 
                (List<Map<String, Object>>) response.get("results");

        // pega o primeiro item
        Map<String, Object> firstResult = results.get(0);

        // extrai a lista de waypoints verdadeira
        List<Map<String, Object>> waypoints =
                (List<Map<String, Object>>) firstResult.get("waypoints");

        List<RouteOptimizationResponse.OrderedWaypoint> ordered = new ArrayList<>();

        if (waypoints != null) {
        	int ordem = 1;
        	for (Map<String, Object> wp : waypoints) {
                double lat = ((Number) wp.get("lat")).doubleValue();
                double lng = ((Number) wp.get("lng")).doubleValue();

                ordered.add(new RouteOptimizationResponse.OrderedWaypoint(
                        ordem++, lat, lng
                ));
            }
        	
        }
        
        return new RouteOptimizationResponse(ordered);
     }
}