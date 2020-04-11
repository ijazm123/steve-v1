package com.gnrgy.ocppserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnrgy.ocppserver.repository.CustomTransactionRepository;
import com.gnrgy.ocppserver.service.CustomTransactionService;

@Service
public class CustomTransactionServiceImpl implements CustomTransactionService{

	@Autowired private CustomTransactionRepository customTransactionRepository;
	
	@Override
	public List<Integer> getActiveTransactionIds(String chargeBoxId, Integer connectorId) {
		return customTransactionRepository.getActiveTransactionIds(chargeBoxId, connectorId);
	}

	@Override
	public Integer getActiveTransactionIdsByTransactionId(String chargeBoxId, Integer connectorId,
			Integer transactionId) {
		return customTransactionRepository.getActiveTransactionIdsByTransactionId(chargeBoxId, connectorId, transactionId);
	}

	
}
