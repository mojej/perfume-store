package com.kej.perfume_store.model;

import java.time.LocalDateTime;

public class MallData {
	private Integer mallId;
	private String mallKey;
	private String mallName;
	private String mallType;
	private String mallUrl;
	private LocalDateTime updatedTime;
	
	public MallData(Integer mallId, String mallKey, String mallName, String mallType, String mallUrl,
			LocalDateTime updatedTime) {
		super();
		this.mallId = mallId;
		this.mallKey = mallKey;
		this.mallName = mallName;
		this.mallType = mallType;
		this.mallUrl = mallUrl;
		this.updatedTime = updatedTime;
	}
	
	public Integer getMallId() {
		return mallId;
	}
	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}
	public String getMallKey() {
		return mallKey;
	}
	public void setMallKey(String mallKey) {
		this.mallKey = mallKey;
	}
	public String getMallName() {
		return mallName;
	}
	public void setMallName(String mallName) {
		this.mallName = mallName;
	}
	public String getMallType() {
		return mallType;
	}
	public void setMallType(String mallType) {
		this.mallType = mallType;
	}
	public String getMallUrl() {
		return mallUrl;
	}
	public void setMallUrl(String mallUrl) {
		this.mallUrl = mallUrl;
	}
	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}
