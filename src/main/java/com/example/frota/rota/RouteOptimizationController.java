package com.example.frota.rota;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/percursos")
@RequiredArgsConstructor
public class RouteOptimizationController {

    private final RouteOptimizationService routeOptimizationService;

    @PostMapping("/otimizar")
    public RouteOptimizationResponse optimize(@RequestBody RouteOptimizationRequest request) {
        return routeOptimizationService.optimize(request);
    }
}
