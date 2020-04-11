package com.gnrgy.ocppserver.web.dto.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RFIDRequest {
	
	   private Boolean allowConcurrentTx;
	   private Boolean allowPublicCharging;
	   private Integer custId;
	   private Date expiresAt;
	   private Boolean isActive;
	   private String label;
	   private String parentId;
	   private String uid;
	   
	@Override
	public String toString() {
		return "RFIDRequest [allowConcurrentTx=" + allowConcurrentTx + ", allowPublicCharging=" + allowPublicCharging
				+ ", custId=" + custId + ", expiresAt=" + expiresAt + ", isActive=" + isActive + ", label=" + label
				+ ", parentId=" + parentId + ", uid=" + uid + "]";
	}
	   
	   
}
