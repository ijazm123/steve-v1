package com.gnrgy.ocppserver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnrgy.ocppserver.exception.GnrgyOcppserverException;
import com.gnrgy.ocppserver.repository.CustomChargePointRepository;
import com.gnrgy.ocppserver.service.CustomChargePointHelperService;

import de.rwth.idsg.steve.ocpp.CommunicationTask;
import de.rwth.idsg.steve.ocpp.OcppVersion;
import de.rwth.idsg.steve.repository.TaskStore;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import ocpp.cs._2015._10.RegistrationStatus;

@Service
public class CustomChargePointHelperServiceImpl implements CustomChargePointHelperService {
	
	@Autowired
	protected ChargePointHelperService chargePointHelperService;
	
	@Autowired
	private TaskStore taskStore;
	

	public List<ChargePointSelect> getchargePointSelectList(String chargeBoxId) throws GnrgyOcppserverException {
		List<ChargePointSelect> chargePointSelectList = new ArrayList<>();
		List<RegistrationStatus> inStatusFilter = Arrays.asList(RegistrationStatus.ACCEPTED,
				RegistrationStatus.PENDING);
		for (ChargePointSelect cps : chargePointHelperService.getChargePoints(OcppVersion.V_16, inStatusFilter)) {
			if (cps.getChargeBoxId().equals(chargeBoxId)) {
				chargePointSelectList.add(cps);
				break;
			}
		}
		if (chargePointSelectList.isEmpty()) {
			throw new GnrgyOcppserverException("charger not found/connected");
		}
		return chargePointSelectList;
	}

	public CommunicationTask checkTaskFinished(int taskId) {
		CommunicationTask r = null;
		Integer max_attempts = 12*5;
		Integer attempt = 0;
		do {
			try {
				r = taskStore.get(taskId);
				Thread.sleep(5000);
			} catch (Exception e) {
				break;
			}
			attempt++;
		} while (!r.isFinished() || attempt < max_attempts);
		return r;
	}

}
