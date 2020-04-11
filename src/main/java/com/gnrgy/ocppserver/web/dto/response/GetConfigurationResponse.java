package com.gnrgy.ocppserver.web.dto.response;

import java.util.List;

import de.rwth.idsg.steve.ocpp.task.GetConfigurationTask;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConfigurationResponse {

	private String status;
    private List<GetConfigurationTask.KeyValue> configurationKey;
    private String unknownKey; 
}
