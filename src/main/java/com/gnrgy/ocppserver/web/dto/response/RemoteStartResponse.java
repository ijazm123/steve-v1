package com.gnrgy.ocppserver.web.dto.response;

public class RemoteStartResponse {
	
	private String status;
	private String session;
	private Integer transaction;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public Integer getTransaction() {
		return transaction;
	}
	public void setTransaction(Integer transaction) {
		this.transaction = transaction;
	}
}
