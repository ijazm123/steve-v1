package com.gnrgy.ocppserver.web.dto.request;

import lombok.Getter;

@Getter
public class RemoteStartRequest {

	private String connector;
	private String identifier;
	
	@Override
	public String toString() {
		return "RemoteStartRequest [connector=" + connector + ", identifier=" + identifier + "]";
	}
	
	
	
}
