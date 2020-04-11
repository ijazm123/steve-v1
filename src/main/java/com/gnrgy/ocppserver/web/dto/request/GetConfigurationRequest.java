package com.gnrgy.ocppserver.web.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConfigurationRequest {

	String chargeBoxId;
	List<String> key;
	
	@Override
	public String toString() {
		return "GetConfigurationRequest [chargeBoxId=" + chargeBoxId + ", key=" + key + "]";
	}
	
}