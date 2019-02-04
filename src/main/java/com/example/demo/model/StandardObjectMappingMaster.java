package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Standard_Object_Mapping_Master")
public class StandardObjectMappingMaster {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "common_format_field_name")
	private String commonFormatFieldName;

	public StandardObjectMappingMaster() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommonFormatFieldName() {
		return commonFormatFieldName;
	}

	public void setCommonFormatFieldName(String commonFormatFieldName) {
		this.commonFormatFieldName = commonFormatFieldName;
	}

}
