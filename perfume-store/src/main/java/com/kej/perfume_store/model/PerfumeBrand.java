package com.kej.perfume_store.model;

import java.util.List;

public class PerfumeBrand {
	private Integer brandId;
	private String processedName;
	private List<Object> collectedName;
	private List<Object> storeId;
	
	public PerfumeBrand() {}

	public PerfumeBrand(Integer brandId, String processedName, List<Object> collectedName, List<Object> storeId) {
		super();
		this.brandId = brandId;
		this.processedName = processedName;
		this.collectedName = collectedName;
		this.storeId = storeId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getProcessedName() {
		return processedName;
	}

	public void setProcessedName(String processedName) {
		this.processedName = processedName;
	}

	public List<Object> getCollectedName() {
		return collectedName;
	}

	public void setCollectedName(List<Object> collectedName) {
		this.collectedName = collectedName;
	}

	public List<Object> getStoreId() {
		return storeId;
	}

	public void setStoreId(List<Object> storeId) {
		this.storeId = storeId;
	}

}
