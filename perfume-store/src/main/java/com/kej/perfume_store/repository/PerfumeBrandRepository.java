package com.kej.perfume_store.repository;

import java.util.List;

import com.kej.perfume_store.model.PerfumeBrand;

public interface PerfumeBrandRepository {
	int update(PerfumeBrand brnad);
	List<PerfumeBrand> findAll();
	PerfumeBrand findById(Integer brandId);
}
