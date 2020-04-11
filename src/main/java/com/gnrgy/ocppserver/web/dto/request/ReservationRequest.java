package com.gnrgy.ocppserver.web.dto.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

	private String cp;
	private Integer connector;
	private Date expires;
	private String identifier;
	
	@Override
	public String toString() {
		return "ReservationRequest [cp=" + cp + ", connector=" + connector + ", expires=" + expires + ", identifier="
				+ identifier + "]";
	}
	
}
