package com.gnrgy.ocppserver.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gnrgy.ocppserver.exception.GnrgyOcppserverException;
import com.gnrgy.ocppserver.service.CustomChargePointHelperService;
import com.gnrgy.ocppserver.service.CustomTransactionService;
import com.gnrgy.ocppserver.web.dto.request.ChangeConfigurationRequest;
import com.gnrgy.ocppserver.web.dto.request.GetConfigurationRequest;
import com.gnrgy.ocppserver.web.dto.request.RFIDRequest;
import com.gnrgy.ocppserver.web.dto.response.ChangeConfigurationResponse;
import com.gnrgy.ocppserver.web.dto.response.GetConfigurationResponse;
import com.gnrgy.ocppserver.web.dto.response.RFIDResponse;
import com.gnrgy.ocppserver.web.dto.response.RemoteStartResponse;

import de.rwth.idsg.steve.ocpp.CommunicationTask;
import de.rwth.idsg.steve.ocpp.OcppVersion;
import de.rwth.idsg.steve.ocpp.RequestResult;
import de.rwth.idsg.steve.ocpp.task.GetConfigurationTask;
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.repository.TaskStore;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.service.ChargePointService16_Client;
import de.rwth.idsg.steve.web.dto.ocpp.ChangeConfigurationParams;
import de.rwth.idsg.steve.web.dto.ocpp.GetConfigurationParams;
import lombok.extern.slf4j.Slf4j;
import ocpp.cs._2015._10.RegistrationStatus;

@Slf4j
@Controller
@RequestMapping(value = "/portal")
public class PortalRestController {

	private static final String GET_CONFIGURATION= "/get_configuration";
	private static final String CHANGE_CONFIGURATION="/change_configuration";
	private static final String UPDATE_RFID_STATUS="/update_rfid_status";
	private static final String ASSOCIATE_RFID_TO_CUSTOMER="/associate_rfid_to_customer";

	@Autowired
	@Qualifier("ChargePointService16_Client")
	private ChargePointService16_Client client16;

	@Autowired
	private TaskStore taskStore;

	@Autowired
	protected ChargePointHelperService chargePointHelperService;
	
	@Autowired private CustomChargePointHelperService customChargePointHelperService;

	protected ChargePointService16_Client getClient16() {
		return client16;
	}
	
	@RequestMapping(value = GET_CONFIGURATION, method = RequestMethod.POST)
	public @ResponseBody GetConfigurationResponse getConfiguration(@RequestBody GetConfigurationRequest getConfigRequest,HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), getConfigRequest.toString());
			GetConfigurationParams getConfigurationParams = new GetConfigurationParams();
			
			getConfigurationParams
					.setChargePointSelectList(customChargePointHelperService.getchargePointSelectList(getConfigRequest.getChargeBoxId()));
			getConfigurationParams.setConfKeyList(getConfigRequest.getKey());
			int taskId = getClient16().getConfiguration(getConfigurationParams);

			CommunicationTask r = customChargePointHelperService.checkTaskFinished(taskId);
			return populateGetConfiguration(r,getConfigRequest.getChargeBoxId());
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = CHANGE_CONFIGURATION, method = RequestMethod.POST)
	public @ResponseBody ChangeConfigurationResponse setConfiguration(@RequestBody ChangeConfigurationRequest changeConfigRequest,HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), changeConfigRequest.toString());
			ChangeConfigurationParams changeConfigurationParams = new ChangeConfigurationParams();
			
			changeConfigurationParams
					.setChargePointSelectList(customChargePointHelperService.getchargePointSelectList(changeConfigRequest.getChargeBoxId()));
			changeConfigurationParams.setConfKey(changeConfigRequest.getKey());
			changeConfigurationParams.setValue(changeConfigRequest.getValue());
			int taskId = getClient16().changeConfiguration(changeConfigurationParams);

			CommunicationTask r = customChargePointHelperService.checkTaskFinished(taskId);
			return populateChangeConfiguration(r,changeConfigRequest.getChargeBoxId());
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	@RequestMapping(value = UPDATE_RFID_STATUS, method = RequestMethod.POST)
	public @ResponseBody List<RFIDResponse> updateRFIDStatus(@RequestBody List<RFIDRequest> rfidRequestList,
			HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), rfidRequestList.toString());
			List<RFIDResponse> rfidResponseList = new ArrayList<>();
			for(RFIDRequest rfidRequest:rfidRequestList) {
				RFIDResponse rfidResponse = new RFIDResponse();
				rfidResponse.setStatus("Updated");
				rfidResponse.setUid(rfidRequest.getUid());
				rfidResponseList.add(rfidResponse);
			}
			return rfidResponseList;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = ASSOCIATE_RFID_TO_CUSTOMER, method = RequestMethod.POST)
	public @ResponseBody List<RFIDResponse> associateRFIDToCustomer(@RequestBody List<RFIDRequest> rfidRequestList,
			HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), rfidRequestList.toString());
			List<RFIDResponse> rfidResponseList = new ArrayList<>();
			for(RFIDRequest rfidRequest:rfidRequestList) {
				RFIDResponse rfidResponse = new RFIDResponse();
				rfidResponse.setStatus("Nothing changed");
				rfidResponse.setUid(rfidRequest.getUid());
				rfidResponseList.add(rfidResponse);
			}
			return rfidResponseList;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	public GetConfigurationResponse populateGetConfiguration(CommunicationTask r,String chargeBoxId) {
		GetConfigurationResponse response = new GetConfigurationResponse();
		RequestResult reqResult = (RequestResult) r.getResultMap().get(chargeBoxId);
		if (reqResult != null && reqResult.getResponse() != null) {
			if (reqResult.getResponse().equals("OK")) {
				response.setStatus("ok");
				GetConfigurationTask.ResponseWrapper getConfigurationResponse = reqResult.getDetails();
				if(!getConfigurationResponse.getConfigurationKeys().isEmpty()) {
					response.setConfigurationKey(getConfigurationResponse.getConfigurationKeys());
				}
				if(getConfigurationResponse.getUnknownKeys()!=null) {
					response.setUnknownKey(getConfigurationResponse.getUnknownKeys());
				}
			} else {
				response.setStatus("ko");
			}
		} else {
			throw new GnrgyOcppserverException("Response not received from chargers");
		}
		return response;
	}
	
	public ChangeConfigurationResponse populateChangeConfiguration(CommunicationTask r,String chargeBoxId) {
		ChangeConfigurationResponse response = new ChangeConfigurationResponse();
		RequestResult reqResult = (RequestResult) r.getResultMap().get(chargeBoxId);
		if (reqResult != null && reqResult.getResponse() != null) {
			response.setStatus(reqResult.getResponse());
		} else {
			throw new GnrgyOcppserverException("Response not received from chargers");
		}
		return response;
	}

}
