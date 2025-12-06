package com.example.frota.rota;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteOptimizationResponse {
    private List<OrderedWaypoint> waypointsOrdered;

    @Data
    @AllArgsConstructor
    public static class OrderedWaypoint {
        private int ordem;
        private double lat;
        private double lng;
    }
}
