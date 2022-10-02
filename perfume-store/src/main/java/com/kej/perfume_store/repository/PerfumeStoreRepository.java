package com.kej.perfume_store.repository;

import java.util.List;
import java.util.Optional;

import com.kej.perfume_store.model.PerfumeStore;

public interface PerfumeStoreRepository {
	List<PerfumeStore> findByName(String brandName);
	PerfumeStore findByUk(String storeKey);
	int upsert(PerfumeStore store);
	int update(PerfumeStore store);
	List<PerfumeStore> findAll();
	int countByUk(String storeKey);
	PerfumeStore findById(Integer storeId);
}
