package org.tnt.pt.entity;

import java.util.Date;

public class CustomerLog {

	private Long id;
	
	private String newCustomer;
	
	private String oldCustomer;
	
	private String userName;
	
	private String describeString;
	
	private Long busId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(String newCustomer) {
		this.newCustomer = newCustomer;
	}

	public String getOldCustomer() {
		return oldCustomer;
	}

	public void setOldCustomer(String oldCustomer) {
		this.oldCustomer = oldCustomer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescribeString() {
		return describeString;
	}

	public void setDescribeString(String describeString) {
		this.describeString = describeString;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}
	
	
}
