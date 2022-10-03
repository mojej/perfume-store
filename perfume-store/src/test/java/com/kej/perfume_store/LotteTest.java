package com.kej.perfume_store;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.service.collect.LotteService;

@SpringBootTest
public class LotteTest {

	@Autowired LotteService lotteService;
	
	@Test
	public void test1() {
		Map<String, Map<String, Object>> branchMap = lotteService.collectBranch();
		for(Map.Entry<String, Map<String, Object>> map : branchMap.entrySet()) {
			String cstrCd = map.getKey();
			if(map.getValue().get("list") == null) {
				Map<String, List<String>> floorsMap = lotteService.collectFloorsByBranch(cstrCd);
				
				for(Map.Entry<String, List<String>> floors : floorsMap.entrySet()) {
					branchMap.get(floors.getKey()).put("list", floors.getValue());
				}
			}
		}
//		System.out.println(branchMap);
	}
	
	@Test
	public void test2() { 
		lotteService.collectFloorsByBranch("0001");
	}
	
	@Test
	public void test3() {
		lotteService.collectStore();
	}
	
}
