package com.example.demo.pojos;

public class CommonFormatAttributeMetaData {
	
	String attributeName;
	boolean isRequired;
	String dataType;
	
	
	public CommonFormatAttributeMetaData(String attributeName, boolean isRequired, String dataType) {
		super();
		this.attributeName = attributeName;
		this.isRequired = isRequired;
		this.dataType = dataType;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
