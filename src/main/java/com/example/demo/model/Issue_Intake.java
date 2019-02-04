package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Issue_Intake {
	public Issue_Intake() {
	}

	@Id
	private String id;
	private String mappedJson;
	
	public Issue_Intake(String id, String mappedJson) {
//		super();
		this.id = id;
		this.mappedJson = mappedJson;
	}
	public String getId() {
		return id;
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
