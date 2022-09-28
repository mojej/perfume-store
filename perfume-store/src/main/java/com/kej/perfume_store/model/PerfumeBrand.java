package com.kej.perfume_store.model;

import java.util.ArrayList;
import java.util.List;

public class PerfumeBrand {
	private Integer brandId;
	private String processedName;
	private List<String> collectedName = new ArrayList<>();
	private List<Integer> storeId = new ArrayList<>();
	
	public PerfumeBrand() {}

	public PerfumeBrand(Integer brandId, String processedName, List<String> collectedName, List<Integer> storeId) {
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

	public List<String> getCollectedName() {
		return collectedName;
	}

	public void setCollectedName(List<String> collectedName) {
		this.collectedName = collectedName;
	}

	public List<Integer> getStoreId() {
		return storeId;
	}

	public void setStoreId(List<Integer> storeId) {
		this.storeId = storeId;
	}

}
