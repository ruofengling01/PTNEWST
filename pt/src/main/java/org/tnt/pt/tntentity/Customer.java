package org.tnt.pt.tntentity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.tnt.pt.entity.IdEntity;


public class Customer extends IdEntity {

	private String accountNo;
	
	private String name;
	
	private String customer_name;
	
	private String branch;
	
	private String territory;
	
	private String channel;
	
	private String rpaccount;
	
	private String bandWidth;
	
	private String cash;
	
	private String qualified;
	
	private String importmark;
	
	private String exportmark;
	
	private String creator;
	
	private String create_time;
	
	private String rpu_id;

	
	public String getCustomer_name() {
		return customer_name;
	}

	
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}



	public String getRpu_id() {
		return rpu_id;
	}



	public void setRpu_id(String rpu_id) {
		this.rpu_id = rpu_id;
	}



	public String getAccountNo() {
		return accountNo;
	}



	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getBranch() {
		return branch;
	}



	public void setBranch(String branch) {
		this.branch = branch;
	}



	public String getTerritory() {
		return territory;
	}



	public void setTerritory(String territory) {
		this.territory = territory;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public String getRpaccount() {
		return rpaccount;
	}



	public void setRpaccount(String rpaccount) {
		this.rpaccount = rpaccount;
	}



	public String getBandWidth() {
		return bandWidth;
	}



	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}



	public String getCash() {
		return cash;
	}



	public void setCash(String cash) {
		this.cash = cash;
	}



	public String getQualified() {
		return qualified;
	}



	public void setQualified(String qualified) {
		this.qualified = qualified;
	}



	public String getImportmark() {
		return importmark;
	}



	public void setImportmark(String importmark) {
		this.importmark = importmark;
	}



	public String getExportmark() {
		return exportmark;
	}



	public void setExportmark(String exportmark) {
		this.exportmark = exportmark;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}