package com.kej.perfume_store.repository;

import java.util.List;
import java.util.Optional;

import com.kej.perfume_store.model.PerfumeStore;

public interface PerfumeStoreRepository {
	int save(PerfumeStore store);
	int update(PerfumeStore store);
	List<PerfumeStore> findAll();
	List<PerfumeStore> findByName(String brandName);
	int countByUk(String storeKey);
	PerfumeStore findByUk(String storeKey);
}
