package com.gnrgy.ocppserver.service;

import java.util.List;

import com.gnrgy.ocppserver.exception.GnrgyOcppserverException;

import de.rwth.idsg.steve.ocpp.CommunicationTask;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;

public interface CustomChargePointHelperService {

	public List<ChargePointSelect> getchargePointSelectList(String chargeBoxId) throws GnrgyOcppserverException;
	
	public CommunicationTask checkTaskFinished(int taskId);
	
}
