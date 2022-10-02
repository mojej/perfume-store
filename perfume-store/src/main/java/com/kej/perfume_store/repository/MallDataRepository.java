package com.kej.perfume_store.repository;

import java.util.List;

import com.kej.perfume_store.model.MallData;

public interface MallDataRepository {
	MallData findById(Integer mallId);
	int update(MallData mallData);
	List<MallData> findAll();
	List<MallData> findByType(String mallType);
}
