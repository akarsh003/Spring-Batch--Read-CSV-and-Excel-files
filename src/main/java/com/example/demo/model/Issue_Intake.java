package com.example.demo.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Issue_Intake {
	public Issue_Intake() {
	}

	@Id
	private String id;
	private String mappedJson;
	private java.util.UUID uuid;
	
	public Issue_Intake(String id, String mappedJson, UUID uuid) {
//		super();
		this.id = id;
		this.mappedJson = mappedJson;
		this.uuid = uuid;
	}
//	
	public java.util.UUID getUuid() {
		return uuid;
	}
	public void setUuid(java.util.UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getId() {
		return id;
	}
	public Issue_Intake(String id, String mappedJson) {
		super();
		this.id = id;
		this.mappedJson = mappedJson;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMappedJson() {
		return this.mappedJson;
	}
	public void setMappedJson(String mappedJson) {
		this.mappedJson = mappedJson;
	}
	
	
}
