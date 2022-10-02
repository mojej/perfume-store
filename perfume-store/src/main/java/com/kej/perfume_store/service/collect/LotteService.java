package com.kej.perfume_store.service.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kej.perfume_store.repository.MallDataRepository;
import com.kej.perfume_store.repository.PerfumeStoreRepository;
import com.kej.perfume_store.util.HttpClient;

@Service
public class LotteService {
	private Logger log = LoggerFactory.getLogger(LotteService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	@Qualifier("jdbcMallDataRepository") // Test JdbcTemplate
	private MallDataRepository mallRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository") // Test JdbcTemplate
	private PerfumeStoreRepository storeRepository;

	@Autowired
	HttpClient httpClient;

	public void collectMall() {
		String url = "https://www.lotteshopping.com/branchFloorGuide/branchListAjax?_format=json";
		String json = httpClient.get(url, null);
		try {
			Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
			if (map.get("list") != null) {
				Map<String, List<Map<String, Object>>> branchMap = (Map<String, List<Map<String, Object>>>) map.get("list");

				for (Map.Entry<String, List<Map<String, Object>>> map1 : branchMap.entrySet()) {
					for (Map<String, Object> val : map1.getValue()) {
						String mdclsDtlCdNm = val.get("mdclsDtlCdNm").toString();
						String lrclsDtlCdNm = val.get("lrclsDtlCdNm").toString();
						String cstrDspNm = val.get("cstrDspNm").toString();
						String cstrCd = val.get("cstrCd").toString();
						System.out.println(mdclsDtlCdNm + " / " + lrclsDtlCdNm + " / " + cstrDspNm);
					}
				}

			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void collectStore() {
		String cstrCd = "";
//		String storeUrl = "https://www.lotteshopping.com/branchFloorGuide/floorShopList?_format=json&cstrCd=" + cstrCd
//				+ "&cstrTownCd=C00401&flrCd=01";

		String townUrl = "https://www.lotteshopping.com/branchShopGuide/shopListAjax?_format=json&cstrCd=" + cstrCd;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("cstrCd", "0001");
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https").host("www.lotteshopping.com")
				.pathSegment("branchShopGuide", "shopListAjax").queryParams(params).build();
		String response = httpClient.get(uriComponents.toString(), null);
		
		Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		List<Map<String, Map<String, Map<String,Object>>>> shopList = null;
		for(Map.Entry<String, Object> val : map.entrySet()) {
			if(val.getKey().equals("shopList"))
				shopList = (List<Map<String, Map<String, Map<String,Object>>>>) val.getValue();
		}
		
		// 빌딩 코드(?)와 층수 담겨있는 맵
		Map<String, List<String>> flrCdMap = new HashMap<>();
		for(Map<String, Map<String, Map<String,Object>>> shop : shopList) { // building cd ?
			
			for(Map.Entry<String, Map<String, Map<String,Object>>> map1 : shop.entrySet()) { // C00401
				String bdCd = map1.getKey();
				// floorList
				List<String> floorList = new ArrayList<>();
				
				for(Map.Entry<String,Map<String,Object>> map2 : map1.getValue().entrySet()) { // floor, type
				
					for(Map.Entry<String, Object> map3 : map2.getValue().entrySet()) {
						if(map3.getKey().equals("floor")) {
							List<Map<String, String>> map3List = (List<Map<String, String>>) map3.getValue();
							for(Map<String, String> floor : map3List) {
								floorList.add(floor.get("cstrFlrCd"));
							}
						}
					}
				}
				
				flrCdMap.put(bdCd, floorList);
			}
		}
		System.out.println(flrCdMap);
	}
}
