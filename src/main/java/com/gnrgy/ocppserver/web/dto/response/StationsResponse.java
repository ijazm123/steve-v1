package com.gnrgy.ocppserver.web.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class StationsResponse {

	private Integer siteId;
	private String label;
	private AddressResponse address;
	private CoordinatesResponse coordinates;
	private Integer connectors_total;
	private Integer connectors_free;
	private Integer connectors_faulted;
	private Integer connectors_reserved;
	private Integer connectors_occupied;
	private List<ConnectorResponse> connectors;
	
	public StationsResponse(Integer siteId, String label, AddressResponse address, CoordinatesResponse coordinates,
			Integer connectors_total, Integer connectors_free, Integer connectors_faulted, Integer connectors_reserved,
			Integer connectors_occupied, List<ConnectorResponse> connectors) {
		super();
		this.siteId = siteId;
		this.label = label;
		this.address = address;
		this.coordinates = coordinates;
		this.connectors_total = connectors_total;
		this.connectors_free = connectors_free;
		this.connectors_faulted = connectors_faulted;
		this.connectors_reserved = connectors_reserved;
		this.connectors_occupied = connectors_occupied;
		this.connectors = connectors;
	}
	
	
}
