package org.tnt.pt.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DiscountDefault extends IdEntity {
	private Long discount;
	private Long productId;
	private Long zoneGroupId;
	
	public DiscountDefault() {
	}

	public DiscountDefault(Long id) {
		this.id = id;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}


	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(Long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}