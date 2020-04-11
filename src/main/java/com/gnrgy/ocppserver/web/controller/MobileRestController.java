package com.gnrgy.ocppserver.web.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.gnrgy.ocppserver.exception.GnrgyOcppserverException;
import com.gnrgy.ocppserver.repository.CustomChargePointRepository;
import com.gnrgy.ocppserver.service.CustomChargePointHelperService;
import com.gnrgy.ocppserver.service.CustomTransactionService;
import com.gnrgy.ocppserver.web.dto.ConnectorUuid;
import com.gnrgy.ocppserver.web.dto.request.PingRequest;
import com.gnrgy.ocppserver.web.dto.request.RemoteStartRequest;
import com.gnrgy.ocppserver.web.dto.request.RemoteStopRequest;
import com.gnrgy.ocppserver.web.dto.request.ReservationRequest;
import com.gnrgy.ocppserver.web.dto.request.SitesRequest;
import com.gnrgy.ocppserver.web.dto.response.AddressResponse;
import com.gnrgy.ocppserver.web.dto.response.ConnectorResponse;
import com.gnrgy.ocppserver.web.dto.response.ConnectorTypeResponse;
import com.gnrgy.ocppserver.web.dto.response.CoordinatesResponse;
import com.gnrgy.ocppserver.web.dto.response.PingResponse;
import com.gnrgy.ocppserver.web.dto.response.RemoteStartResponse;
import com.gnrgy.ocppserver.web.dto.response.RemoteStopResponse;
import com.gnrgy.ocppserver.web.dto.response.ReservationResponse;
import com.gnrgy.ocppserver.web.dto.response.SitesResponse;
import com.gnrgy.ocppserver.web.dto.response.StationsResponse;

import de.rwth.idsg.steve.ocpp.CommunicationTask;
import de.rwth.idsg.steve.ocpp.OcppVersion;
import de.rwth.idsg.steve.ocpp.RequestResult;
import de.rwth.idsg.steve.ocpp.task.ReserveNowTask;
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.repository.TaskStore;
import de.rwth.idsg.steve.repository.dto.ChargePointSelect;
import de.rwth.idsg.steve.repository.dto.ConnectorStatus;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.service.ChargePointService16_Client;
import de.rwth.idsg.steve.service.MailService;
import de.rwth.idsg.steve.web.dto.ConnectorStatusForm;
import de.rwth.idsg.steve.web.dto.ocpp.RemoteStartTransactionParams;
import de.rwth.idsg.steve.web.dto.ocpp.RemoteStopTransactionParams;
import de.rwth.idsg.steve.web.dto.ocpp.ReserveNowParams;
import lombok.extern.slf4j.Slf4j;
import ocpp.cs._2015._10.RegistrationStatus;

@Slf4j
@Controller
@RequestMapping(value = "/mobile")
public class MobileRestController {

	private static final String REMOTE_START = "/remote_start";
	private static final String REMOTE_STOP = "/remote_stop";
	private static final String SITES = "/sites";
	private static final String PING = "/ping";
	private static final String RESERVATION="/reservation";
	
	@Autowired
	@Qualifier("ChargePointService16_Client")
	private ChargePointService16_Client client16;

	@Autowired
	private TaskStore taskStore;

	@Autowired
	private CustomTransactionService customTransactionService;

	@Autowired
	protected ChargePointHelperService chargePointHelperService;
	
	@Autowired private ChargePointRepository chargePointRepository;
	
	@Autowired private CustomChargePointHelperService customChargePointHelperService;

	protected ChargePointService16_Client getClient16() {
		return client16;
	}

	@RequestMapping(value = REMOTE_START, method = RequestMethod.POST)
	public @ResponseBody RemoteStartResponse remoteStart(@RequestBody RemoteStartRequest remoteStartRequest,
			HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), remoteStartRequest.toString());
			ConnectorUuid connectorUuid = new ConnectorUuid(remoteStartRequest.getConnector());
			RemoteStartTransactionParams remoteStartTransactionParams = new RemoteStartTransactionParams();
			remoteStartTransactionParams.setIdTag(remoteStartRequest.getIdentifier());
			remoteStartTransactionParams.setConnectorId(connectorUuid.getConnectorId());
			remoteStartTransactionParams
					.setChargePointSelectList(customChargePointHelperService.getchargePointSelectList(connectorUuid.getChargeBoxId()));
			int taskId = getClient16().remoteStartTransaction(remoteStartTransactionParams);

			CommunicationTask r = customChargePointHelperService.checkTaskFinished(taskId);
			return populateRemoteStartTransaction(r, connectorUuid);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	@RequestMapping(value = REMOTE_STOP, method = RequestMethod.POST)
	public @ResponseBody RemoteStopResponse remoteStop(@RequestBody RemoteStopRequest remoteStopRequest,HttpServletRequest request)
			throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), remoteStopRequest.toString());
			RemoteStopTransactionParams remoteStartTransactionParams = new RemoteStopTransactionParams();
			ConnectorUuid connectorUuid = new ConnectorUuid(remoteStopRequest.getConnector());
			Integer activeTransaction = customTransactionService.getActiveTransactionIdsByTransactionId(
					connectorUuid.getChargeBoxId(), connectorUuid.getConnectorId(), remoteStopRequest.getTransaction());
			if (activeTransaction != null) {
				remoteStartTransactionParams
						.setChargePointSelectList(customChargePointHelperService.getchargePointSelectList(connectorUuid.getChargeBoxId()));
				remoteStartTransactionParams.setTransactionId(remoteStopRequest.getTransaction());
				int taskId = getClient16().remoteStopTransaction(remoteStartTransactionParams);
				CommunicationTask r = customChargePointHelperService.checkTaskFinished(taskId);
				return populateRemoteStopTransaction(r, connectorUuid);
			} else {
				throw new GnrgyOcppserverException("Transaction not found/already stopped");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	@RequestMapping(value = SITES, method = RequestMethod.POST)
	public @ResponseBody SitesResponse sites(@RequestBody SitesRequest siteRequest,
			@RequestParam(required = false) Boolean ignore_portal_configuration,HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), siteRequest.toString());
			List<ConnectorResponse> connectorResponseList=new ArrayList<>();
			List<ConnectorStatus> connectorStatusList = chargePointRepository.getChargePointConnectorStatus(null);
			Integer free=0, faulted=0, reserved=0, occupied=0;
			for(ConnectorStatus connectorStatus:connectorStatusList) {
				ConnectorResponse connectorResponse  = new ConnectorResponse(connectorStatus.getChargeBoxId(),
						
						ConnectorUuid.getConnectorUuid(connectorStatus.getChargeBoxId(), connectorStatus.getConnectorId()),
						ConnectorUuid.getConnectorUuid(connectorStatus.getChargeBoxId(), connectorStatus.getConnectorId()),
						"Type2",null,connectorStatus.getStatus(),true,null,new ConnectorTypeResponse("Outlet 1 (-1)",22000, "22 kW", "sType2","Type 2","AC Charging ≤22kW", "sType2", "AC"));
				connectorResponseList.add(connectorResponse);
				
				if(connectorStatus.getStatus().equals("Available")) {free++;}			
				if(connectorStatus.getStatus().equals("Faulted")) {faulted++;}
				if(connectorStatus.getStatus().equals("Reserved")) {reserved++;}
				if(connectorStatus.getStatus().equals("Charging")) {occupied++;}
				
			}
			
			List<StationsResponse> stationsList =new ArrayList<StationsResponse>();
			stationsList.add(new StationsResponse(25,"חניון גולדה- אחוזות החוף",new AddressResponse("ברקוביץ 7","תל אביב - יפו, ישראל", "Israel"),
					new CoordinatesResponse(32.076527, 34.785503), connectorStatusList.size(), free, faulted, reserved, occupied,connectorResponseList));
			
			SitesResponse siteResponse = new SitesResponse();
			siteResponse.setStatus("NoError");
			siteResponse.setStations(stationsList);
	
			return siteResponse;
		}catch(Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = PING, method = RequestMethod.POST)
	public @ResponseBody PingResponse ping(@RequestBody PingRequest pingRequest,
			@RequestParam(required = false) Boolean ignore_portal_configuration, HttpServletRequest request)
			throws Exception {
		try {
			PingResponse pingResponse = new PingResponse();
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), pingRequest.toString());
			if (pingRequest.getSerialNumber() != null) {
				long sentMillisSerialNumber = System.currentTimeMillis();
				try {
					customChargePointHelperService.getchargePointSelectList(pingRequest.getSerialNumber());
					pingResponse.setStatus("OK");
				}catch(GnrgyOcppserverException e) {
					pingResponse.setStatus("Unreachable");
				}
				pingResponse.setLatency(String.valueOf(System.currentTimeMillis() - sentMillisSerialNumber));
				return pingResponse;
			} else if (pingRequest.getIpAddress() != null) {
				long sentMillisIpAdress = System.currentTimeMillis();
				InetAddress inet = InetAddress.getByName(pingRequest.getIpAddress());
				if(inet.isReachable(5000)) {
					pingResponse.setStatus("OK");
				}else {
					pingResponse.setStatus("Unreachable");
				}
				pingResponse.setLatency(String.valueOf(System.currentTimeMillis() - sentMillisIpAdress));
				return pingResponse;
			} else {
				throw new GnrgyOcppserverException("Request not contains required parameters");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = RESERVATION, method = RequestMethod.POST)
	public @ResponseBody ReservationResponse reservation(@RequestBody ReservationRequest reservationRequest,
			HttpServletRequest request) throws Exception {
		try {
			log.info("Request: {} raised with parameters {}", request.getRequestURL(), reservationRequest.toString());
		
			ReserveNowParams reserveNowParams = new ReserveNowParams();
			reserveNowParams.setChargePointSelectList(customChargePointHelperService.getchargePointSelectList(reservationRequest.getCp()));
			reserveNowParams.setConnectorId(reservationRequest.getConnector());
			reserveNowParams.setExpiry(LocalDateTime.fromDateFields(reservationRequest.getExpires()));
			reserveNowParams.setIdTag(reservationRequest.getIdentifier());
			int taskId = getClient16().reserveNow(reserveNowParams);

			CommunicationTask r = customChargePointHelperService.checkTaskFinished(taskId);
			return populateReservationResponse(r, reservationRequest.getCp());
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	public RemoteStartResponse populateRemoteStartTransaction(CommunicationTask r, ConnectorUuid connectorUuid) {
		RemoteStartResponse response = new RemoteStartResponse();
		RequestResult reqResult = (RequestResult) r.getResultMap().get(connectorUuid.getChargeBoxId());
		if (reqResult != null && reqResult.getResponse() != null) {
			if (reqResult.getResponse().equals("Accepted")) {
				response.setStatus("ok");
				// extract transactionId from database
				List<Integer> transactionList = customTransactionService.getActiveTransactionIds(connectorUuid.getChargeBoxId(),
						connectorUuid.getConnectorId());
				if (!transactionList.isEmpty()) {
					Collections.sort(transactionList);
					response.setTransaction(transactionList.get(transactionList.size() - 1));
					response.setSession(UUID.randomUUID().toString());
				} else {
					throw new GnrgyOcppserverException("transaction not found");
				}
			} else {
				response.setStatus("ko");
			}
		} else {
			throw new GnrgyOcppserverException("Response not received from chargers");
		}
		return response;
	}

	public RemoteStopResponse populateRemoteStopTransaction(CommunicationTask r, ConnectorUuid connectorUuid) {
		RemoteStopResponse response = new RemoteStopResponse();
		RequestResult reqResult = (RequestResult) r.getResultMap().get(connectorUuid.getChargeBoxId());
		if (reqResult != null && reqResult.getResponse() != null) {
			if (reqResult.getResponse().equals("Accepted")) {
				response.setStatus("ok");
				// extract transactionId from database
			} else {
				response.setStatus("ko");
			}
		} else {
			throw new GnrgyOcppserverException("Response not received from chargers");
		}
		return response;
	}
	
	public ReservationResponse populateReservationResponse(CommunicationTask r, String cp) {
		ReservationResponse response = new ReservationResponse();
		RequestResult reqResult = (RequestResult) r.getResultMap().get(cp);
		if (reqResult != null && reqResult.getResponse() != null) {
			ReserveNowTask reserveNowTask = (ReserveNowTask)r;
			response.setReservationId(reserveNowTask.getParams().getReservationId());
			response.setStatus(reqResult.getResponse());
		} else {
			throw new GnrgyOcppserverException("Response not received from chargers");
		}
		return response;
	}

}
