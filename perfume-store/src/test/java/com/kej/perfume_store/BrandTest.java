package com.kej.perfume_store;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.BrandService;

@SpringBootTest
public class BrandTest {
	@Autowired BrandService brandService;
	
	@Test
	public void getNameMap() {
		Map<String,Integer> map = brandService.getBrandMappingMap();
		System.out.println(map);
	}
}
