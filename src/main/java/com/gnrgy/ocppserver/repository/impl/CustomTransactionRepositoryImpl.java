package com.gnrgy.ocppserver.repository.impl;

import static jooq.steve.db.tables.Connector.CONNECTOR;
import static jooq.steve.db.tables.Transaction.TRANSACTION;

import java.util.List;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gnrgy.ocppserver.repository.CustomTransactionRepository;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

	private final DSLContext ctx;
	
	@Autowired
    public CustomTransactionRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }
	
	@Override
	public List<Integer> getActiveTransactionIds(String chargeBoxId, Integer connectorId) {
		return ctx.select(TRANSACTION.TRANSACTION_PK)
                .from(TRANSACTION)
                .join(CONNECTOR)
                  .on(TRANSACTION.CONNECTOR_PK.equal(CONNECTOR.CONNECTOR_PK))
                  .and(CONNECTOR.CONNECTOR_ID.equal(connectorId))
                  .and(CONNECTOR.CHARGE_BOX_ID.equal(chargeBoxId))
                .where(TRANSACTION.STOP_TIMESTAMP.isNull())
                .fetch(TRANSACTION.TRANSACTION_PK);
	}
	
	@Override
	public Integer getActiveTransactionIdsByTransactionId(String chargeBoxId, Integer connectorId,Integer transactionId) {
		return ctx.select(TRANSACTION.TRANSACTION_PK)
                .from(TRANSACTION)
                .join(CONNECTOR)
                  .on(TRANSACTION.CONNECTOR_PK.equal(CONNECTOR.CONNECTOR_PK))
                  .and(CONNECTOR.CONNECTOR_ID.equal(connectorId))
                  .and(CONNECTOR.CHARGE_BOX_ID.equal(chargeBoxId))
                .where(TRANSACTION.STOP_TIMESTAMP.isNull())
                .and(TRANSACTION.TRANSACTION_PK.eq(transactionId))
                .fetchOne(TRANSACTION.TRANSACTION_PK);
	}
	
	

}
