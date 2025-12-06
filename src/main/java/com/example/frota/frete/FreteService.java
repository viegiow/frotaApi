package com.example.frota.frete;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.example.frota.errors.ProdutoIncompativelCaixa;
import com.example.frota.errors.ResourceNotFoundException;
import com.example.frota.parametros.Parametro;
import com.example.frota.parametros.ParametroService;
import com.example.frota.produto.Produto;
import com.example.frota.produto.ProdutoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FreteService {
	@Autowired
	private CaixaService caixaService;
	
	@Autowired
	private ProdutoService produtoService;

    @Autowired
	private ParametroService parametroService;
	
    private static final String API_KEY = "AIzaSyAP1IE7RV5OqTAGPNnw2NbI8jNesEnXT1Y";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    
    private static final String API_KEY_HERE = "mcrxZoUUlLNigSmNNbAZQ1zx0xJ67_wZK23Vhp6SGck";
    private static final String BASE_URL_HERE_GEO = "https://geocode.search.hereapi.com/v1/geocode";
    private static final String BASE_URL_HERE = "https://router.hereapi.com/v8/routes";
	
    public double calcularValorPorPeso(Long produtoId, Long caixaId) {
        Parametro parametro = parametroService.procurarPorNome("CUSTO_PESO").orElseThrow(() -> new EntityNotFoundException("Parâmetro de custo por peso não encontrado"));;
        double custoPorPeso = Double.parseDouble(parametro.getValor());
        Produto produto = produtoService.procurarPorId(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        Caixa caixa = caixaService.procurarPorId(caixaId)
                .orElseThrow(() -> new EntityNotFoundException("Caixa não encontrada"));
        
        Double pesoConsiderado = produto.getPesoProduto() > caixa.getPesoCubado() ? produto.getPesoProduto() : caixa.getPesoCubado();
        return pesoConsiderado * custoPorPeso;
    }

    
    public static String removerAcentos(String str) {
        if (str == null) return null;
        String normalizado = Normalizer.normalize(str, Normalizer.Form.NFD);
        String semAcentos = normalizado.replaceAll("\\p{M}", "");

        return semAcentos;
    }

    public double[] obterCoordenadas(String endereco) throws Exception {
    	try {
    		RestTemplate restTemplate = new RestTemplate();
    		String enderecoEncoded  = removerAcentos(endereco);
    		
    		URI uri = UriComponentsBuilder
    				.fromUriString(BASE_URL_HERE_GEO) 
                    .queryParam("q", enderecoEncoded)
                    .queryParam("apikey", API_KEY_HERE)
                    .build()
                    .toUri();
    		
    		String response = restTemplate.getForObject(uri, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("items");

            if (!items.isArray() || items.isEmpty()) return null;
            
            double lat = items.get(0).path("position").path("lat").asDouble();
            double lng = items.get(0).path("position").path("lng").asDouble();
            
            return new double[]{lat, lng};
    	} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String obterEndereco(double lat, double lng) throws JsonMappingException, JsonProcessingException {
    	RestTemplate restTemplate = new RestTemplate();
        String url = String.format(
        		Locale.US,
                "https://revgeocode.search.hereapi.com/v1/revgeocode?at=%f,%f&lang=pt-BR&apiKey=%s",
                lat, lng, API_KEY_HERE
        );

        String respostaJson = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(respostaJson);
        JsonNode items = root.path("items");
        if (!items.isArray() || items.isEmpty()) {
            return null;
        }

        JsonNode item0 = items.get(0);

        // Pega o "label" dentro de address
        String endereco = item0.path("address").path("label").asText();

        if (endereco == null || endereco.isBlank()) {
            return null;
        }

        return endereco;
    }
    
    public Double calcularPedagios(String origem, String destino) {
    	try {
    		RestTemplate restTemplate = new RestTemplate();
    		
            double[] coordOrigem = obterCoordenadas(origem);
            double[] coordDestino = obterCoordenadas(destino);
            
            if (coordOrigem == null || coordDestino == null)
            	throw new RuntimeException("Erro ao obter coordenadas.");
            		
    		URI uri = UriComponentsBuilder
    				.fromUriString(BASE_URL_HERE) 
                    .queryParam("transportMode", "truck")
                    .queryParam("origin", coordOrigem[0] + "," + coordOrigem[1])
                    .queryParam("destination", coordDestino[0] + "," + coordDestino[1])
                    .queryParam("return", "summary,tolls")
                    .queryParam("apikey", API_KEY_HERE)
                    .build()
                    .toUri();
    		
    		String response = restTemplate.getForObject(uri, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            JsonNode tolls = root.path("routes").get(0).path("sections").get(0).path("tolls");
            
            if (tolls.isMissingNode()) {
            	return 0.0;
            }
            else {
            	double totalTolls = 0.0;
                if (tolls.isArray()) {
                    for (JsonNode toll : tolls) {
                        JsonNode fares = toll.path("fares");
                        if (fares.isArray()) {
                            for (JsonNode fare : fares) {
                                double valor = fare.path("price").path("value").asDouble(0.0);
                                totalTolls += valor;
                            }
                        }
                    }
                }
                return totalTolls;
            }
            
            
    	} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        Parametro parametro = parametroService.procurarPorNome("CUSTO_KM").orElseThrow(() -> new EntityNotFoundException("Parâmetro de custo por peso não encontrado"));;
        double custoPorDistancia = Double.parseDouble(parametro.getValor());
        return distancia * custoPorDistancia;
    }
    
    public Double obterTotalFrete(FreteCustoDistancia dados) {
        Produto produto = produtoService.procurarPorId(dados.produtoId())
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
		List<Caixa> caixas = caixaService.procurarCompativeis(produto);
		boolean cabe = caixas.stream()
                .anyMatch(caixa -> caixa.getId().equals(dados.caixaId()));
		if (cabe) {
			Double distancia = calcularDistancia(dados.origem(), dados.destino());
			Double totalPeso = calcularValorPorPeso(dados.produtoId(), dados.caixaId());
	        Double custoDistancia =  calcularCustoPorDistancia(distancia);
	        Double pedagio = calcularPedagios(dados.origem(), dados.destino());
	        Double total = (double) Math.round((totalPeso + custoDistancia + pedagio)*100)/100;
	        return total;
		}
		else {
			throw new ProdutoIncompativelCaixa("Produto não cabe na caixa");
		}
        
    }
}
