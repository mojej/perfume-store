package com.kej.perfume_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.collect.LotteService;

@SpringBootTest
public class LotteTest {

	@Autowired LotteService lotteService;
	
	@Test
	public void test1() {
		lotteService.collectMall();
	}
	
	@Test
	public void test2() { 
		lotteService.collectStore();
	}

}
