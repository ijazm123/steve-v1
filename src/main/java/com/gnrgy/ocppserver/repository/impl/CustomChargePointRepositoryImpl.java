package com.gnrgy.ocppserver.repository.impl;

import static jooq.steve.db.tables.ChargeBox.CHARGE_BOX;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gnrgy.ocppserver.repository.CustomChargePointRepository;

@Repository
public class CustomChargePointRepositoryImpl implements CustomChargePointRepository{

	private final DSLContext ctx;
	
	@Autowired
    public CustomChargePointRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }
	
	@Override
	public String getChargeBoxId(String serialNumber) {
		 return ctx.select(CHARGE_BOX.CHARGE_BOX_ID)
         .from(CHARGE_BOX)
         .where(CHARGE_BOX.CHARGE_BOX_SERIAL_NUMBER.eq(serialNumber))
         .fetchOne(CHARGE_BOX.CHARGE_BOX_ID);
	}

}
