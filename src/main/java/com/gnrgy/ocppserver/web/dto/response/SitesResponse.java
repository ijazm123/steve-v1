package com.gnrgy.ocppserver.web.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SitesResponse {

	private String status;
	private List<StationsResponse> stations;
}
