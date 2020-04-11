package com.gnrgy.ocppserver.service;

import java.util.List;

public interface CustomTransactionService {

	public List<Integer> getActiveTransactionIds(String chargeBoxId, Integer connectorId);
	
	public Integer getActiveTransactionIdsByTransactionId(String chargeBoxId, Integer connectorId,Integer transactionId);
}
