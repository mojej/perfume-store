package com.kej.perfume_store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kej.perfume_store.model.PerfumeBrand;
import com.kej.perfume_store.repository.PerfumeBrandRepository;
import com.kej.perfume_store.service.collect.ShinsegaeService;

@Service
public class BrandService {
	private Logger log = LoggerFactory.getLogger(ShinsegaeService.class);
//	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	@Qualifier("jdbcPerfumeBrandRepository") // Test JdbcTemplate
	private PerfumeBrandRepository brandRepository;
	
	public Map<String, Integer> getBrandMappingMap() {
		Map<String, Integer> result = new HashMap<>();
		List<PerfumeBrand> brands = brandRepository.findAll();
		for(PerfumeBrand brand:brands) {
			Integer brandId = brand.getBrandId();
			List<String> names = brand.getCollectedName();
			for(String name:names) {
				result.put(name.toString(), brandId);
			}
		}
		return result;
	}

}
