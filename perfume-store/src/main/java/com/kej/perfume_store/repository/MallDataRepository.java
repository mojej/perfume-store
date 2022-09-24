package com.kej.perfume_store.repository;

import java.util.List;

import com.kej.perfume_store.model.MallData;

public interface MallDataRepository {
	int update(MallData mallData);
	List<MallData> findAll();
}
