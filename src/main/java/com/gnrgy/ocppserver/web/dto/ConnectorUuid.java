package com.gnrgy.ocppserver.web.dto;

import lombok.Getter;

@Getter
public class ConnectorUuid {

	private static final String DELIMITER="\\*"; 
	
	private String chargeBoxId;
	private Integer connectorId;
	
	
	public ConnectorUuid(String connectorUuid) {
		String[] parts = connectorUuid.split(DELIMITER);
		this.chargeBoxId=parts[0];
		this.connectorId=Integer.parseInt(parts[1]);
	}
	
	public static String getConnectorUuid(String chargeBoxId,Integer connectorId) {
		return new StringBuilder().append(chargeBoxId).append("*").append(connectorId).toString();
	}
}
