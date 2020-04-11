package com.gnrgy.ocppserver.repository;

import java.util.List;

public interface CustomTransactionRepository {

	List<Integer> getActiveTransactionIds(String chargeBoxId,Integer connectorId);
	
	public Integer getActiveTransactionIdsByTransactionId(String chargeBoxId, Integer connectorId,Integer transactionId);
}
