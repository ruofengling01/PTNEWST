package org.tnt.pt.entity;

import java.util.Date;

public class Exam {

	private Long id;
	
	private Long businessId;
	
	private String examOppion;
	
	private Date examTime;
	
	private String userName;

	private String showTime;
	
	private String filePath = "";
	
	private String fileName = "";
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getExamOppion() {
		return examOppion;
	}

	public void setExamOppion(String examOppion) {
		this.examOppion = examOppion;
	}

	public Date getExamTime() {
		return examTime;
	}

	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
