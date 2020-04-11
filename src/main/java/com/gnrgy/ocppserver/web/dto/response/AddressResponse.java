package com.gnrgy.ocppserver.web.dto.response;

import lombok.Getter;

@Getter
public class AddressResponse {

	private String street;
	private String city;
	private String country;
	
	public AddressResponse(String street, String city, String country) {
		super();
		this.street = street;
		this.city = city;
		this.country = country;
	}
	
	
}
