package com.kej.perfume_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.collect.ShinsegaeService;

@SpringBootTest
public class SsgTest {
	
	@Autowired ShinsegaeService ssgService;
	
	@Test
	public void collect1() {
		ssgService.collectStore();
	}
	
	@Test
	public void test2() {
		ssgService.mappingStoreToBrand();
	}

}
