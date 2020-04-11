package com.gnrgy.ocppserver.web.dto.response;

import lombok.Getter;

@Getter
public class ConnectorTypeResponse {

	private String label;
	private Integer max_power;
	private String max_power_string;
	private String connector_standard;
	private String plug_type;
	private String charging_facility_type;
	private String charging_mod_type;
	private String charge_point_type;
	
	public ConnectorTypeResponse(String label, Integer max_power, String max_power_string, String connector_standard,
			String plug_type, String charging_facility_type, String charging_mod_type, String charge_point_type) {
		super();
		this.label = label;
		this.max_power = max_power;
		this.max_power_string = max_power_string;
		this.connector_standard = connector_standard;
		this.plug_type = plug_type;
		this.charging_facility_type = charging_facility_type;
		this.charging_mod_type = charging_mod_type;
		this.charge_point_type = charge_point_type;
	}
	
	
}
