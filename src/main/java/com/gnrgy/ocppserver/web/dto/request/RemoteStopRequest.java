package com.gnrgy.ocppserver.web.dto.request;

import lombok.Getter;

@Getter
public class RemoteStopRequest {

	private String connector;
	private Integer transaction;
	private String chargingSessionId;

	@Override
	public String toString() {
		return "RemoteStopRequest [connector=" + connector + ", transaction=" + transaction + ", chargingSessionId="
				+ chargingSessionId + "]";
	}
	
}
