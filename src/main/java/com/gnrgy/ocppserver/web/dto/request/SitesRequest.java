package com.gnrgy.ocppserver.web.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SitesRequest {

	private List<String> stationUuids;

	@Override
	public String toString() {
		return "SitesRequest [stationUuids=" + stationUuids + "]";
	}
	
	
}
