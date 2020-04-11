package com.gnrgy.ocppserver.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeConfigurationRequest {
	
	private String chargeBoxId;
	private String key;
	private String value;
	
	@Override
	public String toString() {
		return "ChangeConfigurationRequest [chargeBoxId=" + chargeBoxId + ", key=" + key + ", value=" + value + "]";
	}
	
}
