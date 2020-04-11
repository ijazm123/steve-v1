package com.gnrgy.ocppserver.web.dto.response;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class ReservationResponse {
	
	private String status;
	private Integer reservationId;
}
