package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Axcis_Report_Column")
public class AxcisReportColumn {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "columns")
	private String columns;

	@Column(name = "file_id")
	private int fileId;
	@Column(name = "mapping_Column")
	private int mappingColumn;
	@ManyToOne
	@JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private FileInfo fileInfo;

	@OneToOne
	@JoinColumn(name = "mapping_column", nullable = false, insertable = false, updatable = false)
	private StandardObjectMappingMaster standardObjectMappingMaster;

	public AxcisReportColumn() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getMappingColumn() {
		return mappingColumn;
	}

	public void setMappingColumn(int mappingColumn) {
		this.mappingColumn = mappingColumn;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public StandardObjectMappingMaster getStandardObjectMappingMaster() {
		return standardObjectMappingMaster;
	}

	public void setStandardObjectMappingMaster(StandardObjectMappingMaster standardObjectMappingMaster) {
		this.standardObjectMappingMaster = standardObjectMappingMaster;
	}

}
