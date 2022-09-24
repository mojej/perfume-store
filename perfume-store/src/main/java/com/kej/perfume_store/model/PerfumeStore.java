package com.kej.perfume_store.model;

public class PerfumeStore {
	private Integer storeId;
	private String brandName;
	private String phoneNumber;
	private String rawData;
	private Integer mallId;
	private String storeKey;
	
	public PerfumeStore() {}
	
	public PerfumeStore(Integer storeId, String brandName, String phoneNumber, String rawData, Integer mallId,
			String storeKey) {
		super();
		this.storeId = storeId;
		this.brandName = brandName;
		this.phoneNumber = phoneNumber;
		this.rawData = rawData;
		this.mallId = mallId;
		this.storeKey = storeKey;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public Integer getMallId() {
		return mallId;
	}

	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}
	
}
