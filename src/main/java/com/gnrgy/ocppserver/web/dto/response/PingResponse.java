package com.gnrgy.ocppserver.web.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingResponse {

	private String status;
	private String latency;
}
