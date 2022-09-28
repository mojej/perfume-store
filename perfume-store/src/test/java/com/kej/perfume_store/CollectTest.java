package com.kej.perfume_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.collect.ShinsegaeService;

@SpringBootTest
public class CollectTest {
	
	@Autowired ShinsegaeService collectService;
	
	@Test
	public void collect1() {
		collectService.collectStore();
	}

}
