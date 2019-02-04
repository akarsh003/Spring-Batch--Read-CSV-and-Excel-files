package com.example.demo.pojos;

public class WorkRequestTypeMetaData {
	String workRequestType;
	int workRequestTypeId;
	boolean ignoreAdditionalAttributes;
	CommonFormatAttributeMetaData[] attributes;
	
	
	
	public WorkRequestTypeMetaData() {
		super();
	}
	public WorkRequestTypeMetaData(String workRequestType, int workRequestTypeId, boolean ignoreAdditionalAttributes,
			CommonFormatAttributeMetaData[] attributes) {
		super();
		this.workRequestType = workRequestType;
		this.workRequestTypeId = workRequestTypeId;
		this.ignoreAdditionalAttributes = ignoreAdditionalAttributes;
		this.attributes = attributes;
	}
	public String getWorkRequestType() {
		return workRequestType;
	}
	public void setWorkRequestType(String workRequestType) {
		this.workRequestType = workRequestType;
	}
	public int getWorkRequestTypeId() {
		return workRequestTypeId;
	}
	public void setWorkRequestTypeId(int workRequestTypeId) {
		this.workRequestTypeId = workRequestTypeId;
	}
	public boolean isIgnoreAdditionalAttributes() {
		return ignoreAdditionalAttributes;
	}
	public void setIgnoreAdditionalAttributes(boolean ignoreAdditionalAttributes) {
		this.ignoreAdditionalAttributes = ignoreAdditionalAttributes;
	}
	public CommonFormatAttributeMetaData[] getAttributes() {
		return attributes;
	}
	public void setAttributes(CommonFormatAttributeMetaData[] attributes) {
		this.attributes = attributes;
	}
	
	WorkRequestTypeMetaData getAttributesForWorkRequestType(String workRequestType) {
		//implementation
		return null;
	}
	
	
}
