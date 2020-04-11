package com.gnrgy.ocppserver.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingRequest {

	private String serialNumber; //chargeBoxId
	private String ipAddress;
	
	@Override
	public String toString() {
		return "PingRequest [serialNumber=" + serialNumber + ", ipAddress=" + ipAddress + "]";
	}
	
	
}
