package com.example.frota.rota;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteOptimizationRequest {
	private Coordinate origem;
	private List<Coordinate> destinos;
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Coordinate {
		private double lat;
		private double lng;
	}
}
