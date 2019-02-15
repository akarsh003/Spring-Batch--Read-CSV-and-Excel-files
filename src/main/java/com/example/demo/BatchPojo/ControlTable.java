package com.example.demo.BatchPojo;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ControlTable {

	
	@Id
	private java.util.UUID UUID;
	private String filename;
	private String status;

	
	
public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}


	
	public ControlTable() {
		super();
	}

	
	
	public java.util.UUID  getUUID() {
		return UUID;
	}
	public ControlTable(java.util.UUID uUID, String filename, String status1) {
	super();
	UUID = uUID;
	this.filename = filename;
	status = status1;
}
	public void setUUID(java.util.UUID  uUID) {
		UUID = uUID;
	}
	public String getstatus() {
		return status;
	}


	public void setstatus(String status1) {
		status = status1;
	}
	
}
