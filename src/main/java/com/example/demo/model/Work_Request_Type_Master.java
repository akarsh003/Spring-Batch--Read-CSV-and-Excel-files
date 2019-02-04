package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Work_Request_Type_Master {

	@Id
	private int wId;
	private String workRequestType;
	@JsonProperty
	private boolean ignoreAditionalAttributes;
	
	
	
	
	
	public Work_Request_Type_Master()
	{
		
	}





	public int getwId() {
		return wId;
	}





	public void setwId(int wId) {
		this.wId = wId;
	}





	public String getWorkRequestType() {
		return workRequestType;
	}





	public void setWorkRequestType(String workRequestType) {
		this.workRequestType = workRequestType;
	}





	public boolean getIgnoreAditionalAttributes() {
		return ignoreAditionalAttributes;
	}





	public void setIgnoreAditionalAttributes(boolean ignoreAditionalAttributes) {
		this.ignoreAditionalAttributes = ignoreAditionalAttributes;
	}





	public Work_Request_Type_Master(int wId, String workRequestType, boolean ignoreAditionalAttributes) {
		super();
		this.wId = wId;
		this.workRequestType = workRequestType;
		this.ignoreAditionalAttributes = ignoreAditionalAttributes;
	}

	
	
	
}
