package com.gnrgy.ocppserver.web.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class ConnectorResponse {

	private String chargeBoxId;
	private String connectorUuid;
	private String evseid;
	private String label;
	private String barcode;
	private String status;
	
	private Boolean remote_Start;
	private String charging_session;
	
	private ConnectorTypeResponse connecotorType;
	
	public ConnectorResponse(String chargeBoxId, String connectorUuid, String evseid, String label, String barcode,
			String status, Boolean remote_Start, String charging_session,ConnectorTypeResponse connecotorType) {
		super();
		this.chargeBoxId = chargeBoxId;
		this.connectorUuid = connectorUuid;
		this.evseid = evseid;
		this.label = label;
		this.barcode = barcode;
		this.status = status;
		this.remote_Start = remote_Start;
		this.charging_session = charging_session;
		this.connecotorType=connecotorType;
	}
}
