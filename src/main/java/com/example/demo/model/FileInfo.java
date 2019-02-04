package com.example.demo.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "File_Info")
public class FileInfo {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "file_name")
	private String fileName;

	@OneToMany(mappedBy = "fileInfo", fetch = FetchType.EAGER)
	private Set<AxcisReportColumn> axcisReportColumn;

	@OneToMany(mappedBy = "fileInfo", fetch = FetchType.EAGER)
	private Set<IssueTypeMaster> issueTypeMaster;

	public FileInfo() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Set<AxcisReportColumn> getAxcisReportColumn() {
		return axcisReportColumn;
	}

	public void setAxcisReportColumn(Set<AxcisReportColumn> axcisReportColumn) {
		this.axcisReportColumn = axcisReportColumn;
	}

	public Set<IssueTypeMaster> getIssueTypeMaster() {
		return issueTypeMaster;
	}

	public void setIssueTypeMaster(Set<IssueTypeMaster> issueTypeMaster) {
		this.issueTypeMaster = issueTypeMaster;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * @Override public String toString() { return "FileInfo [id=" + id +
	 * ", fileName=" + fileName + ", axcisReportColumn=" + axcisReportColumn +
	 * ", issueTypeMaster=" + issueTypeMaster + "]"; }
	 */

}
