package com.gnrgy.ocppserver.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CoordinatesResponse {

	private Double lat;
	
	@JsonProperty("long")
	private Double lng;

	public CoordinatesResponse(Double lat, Double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
}
