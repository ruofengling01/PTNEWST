package org.tnt.pt.vo;

import java.util.List;

public class BusinessVO extends BaseVO{

	private String id;
	
	private String account;
	
	private String depot;
	
	private String applicationReference;
	
	private String startDate;
	
	private String endDate;
	
	private String revMonStart;
	
	private String revMonEnd;

	private String cusName;
	
	private String industry;
	
	private String state;
	
	private String examOppion;
	
	private String multichoised;
	
	private String effectiveDate;
	
	private List<String> depotList;
	
	private List<String> statusList;
	
	private String userName;
	
	private String channel;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the depot
	 */
	public String getDepot() {
		return depot;
	}

	/**
	 * @param depot the depot to set
	 */
	public void setDepot(String depot) {
		this.depot = depot;
	}

	/**
	 * @return the applicationReference
	 */
	public String getApplicationReference() {
		return applicationReference;
	}

	/**
	 * @param applicationReference the applicationReference to set
	 */
	public void setApplicationReference(String applicationReference) {
		this.applicationReference = applicationReference;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the revMonStart
	 */
	public String getRevMonStart() {
		return revMonStart;
	}

	/**
	 * @param revMonStart the revMonStart to set
	 */
	public void setRevMonStart(String revMonStart) {
		this.revMonStart = revMonStart;
	}

	/**
	 * @return the revMonEnd
	 */
	public String getRevMonEnd() {
		return revMonEnd;
	}

	/**
	 * @param revMonEnd the revMonEnd to set
	 */
	public void setRevMonEnd(String revMonEnd) {
		this.revMonEnd = revMonEnd;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getExamOppion() {
		return examOppion;
	}

	public void setExamOppion(String examOppion) {
		this.examOppion = examOppion;
	}

	public String getMultichoised() {
		return multichoised;
	}

	public void setMultichoised(String multichoised) {
		this.multichoised = multichoised;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<String> getDepotList() {
		return depotList;
	}

	public void setDepotList(List<String> depotList) {
		this.depotList = depotList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

}
