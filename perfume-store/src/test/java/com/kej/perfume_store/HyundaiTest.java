package com.kej.perfume_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.collect.HyundaiService;

@SpringBootTest
public class HyundaiTest {
	
	@Autowired private HyundaiService hyundaiService;
	
	@Test
	public void test1() {
		hyundaiService.collectStore();
	}

}
