package com.kej.perfume_store.service.collect;

import java.time.LocalDateTime;
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
import com.kej.perfume_store.model.MallData;
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

	@Autowired HttpClient httpClient;
	
	public void collectStore() {
		Map<String, Map<String, Object>> brandWithFloors = collectBrandWithFloors();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("_format", "json");
		params.set("cstrTownCd", "C00401");

		for(Map.Entry<String, Map<String, Object>> branchMap : brandWithFloors.entrySet()) {
			List<String> floors = (List<String>) branchMap.getValue().get("list");
			System.out.println(branchMap.getKey());
			for(String floor : floors) {
				// setting url
				params.set("cstrCd", branchMap.getKey());
				params.set("flrCd", floor);
				UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https").host("www.lotteshopping.com")
						.pathSegment("branchFloorGuide", "floorShopList").queryParams(params).build();
				String response = httpClient.get(uriComponents.toString(), null);
				try {
					Map<String, Object> map = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
					List<Map<String, Object>> shopList1 = (List<Map<String, Object>>) map.get("shopList");
					// 화장품 lrclsCd - 050000
					for(Map<String, Object> shop : shopList1) {
						if(shop.get("050000") != null) {
							List<Map<String, Object>> shopList2 = (List<Map<String, Object>>) shop.get("050000");
							
							for(Map<String, Object> shop2 : shopList2) {
								List<Map<String, Object>> shopList3 = (List<Map<String, Object>>) shop2.get("shopInfoList");
								for(Map<String, Object> shop3 : shopList3) {
									System.out.println(shop3.get("shopNm"));
								}
							}
						}
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	public Map<String, Map<String, Object>> collectBrandWithFloors() {
		Map<String, Map<String, Object>> branchMap = collectBranch();
		for(Map.Entry<String, Map<String, Object>> map : branchMap.entrySet()) {
			String cstrCd = map.getKey();
			if(map.getValue().get("list") == null) {
				Map<String, List<String>> floorsMap = collectFloorsByBranch(cstrCd);
				
				for(Map.Entry<String, List<String>> floors : floorsMap.entrySet()) {
					branchMap.get(floors.getKey()).put("list", floors.getValue());
				}
			}
		}
		return branchMap;
	}
	
	public Map<String, Map<String, Object>> collectBranch() {
		String url = "https://www.lotteshopping.com/branchFloorGuide/branchListAjax?_format=json";
		String json = httpClient.get(url, null);
		
//		{ "0001" : { data: {}, floor: [] } }
		Map<String, Map<String, Object>> processedBrandchMap = new HashMap<>();
		try {
			Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
			
			
	
			if (map.get("list") != null) {
				Map<String, List<Map<String, Object>>> branchMap = (Map<String, List<Map<String, Object>>>) map.get("list");
				for (Map.Entry<String, List<Map<String, Object>>> map1 : branchMap.entrySet()) {
					for (Map<String, Object> val : map1.getValue()) {
						String mdclsDtlCdNm = val.get("mdclsDtlCdNm").toString();
						String lrclsDtlCdNm = val.get("lrclsDtlCdNm").toString();
						String cstrDspNm = val.get("cstrDspNm").toString();
						String cstrCd = val.get("cstrCd").toString();
						
						processedBrandchMap.put(cstrCd, new HashMap<>());
//						processedBrandchMap.get(cstrCd).put("data", val);
						
						String processedName = "";
						
						if(mdclsDtlCdNm.equals("롯데몰")) {
							processedName = mdclsDtlCdNm +" "+ cstrDspNm;
						} else if(lrclsDtlCdNm.equals("기타 매장")) {
							processedName = "롯데 "+ mdclsDtlCdNm +" "+ cstrDspNm;
						} else if(lrclsDtlCdNm.equals("백화점")) {
							processedName = "롯데백화점 " + cstrDspNm;
						} else if(mdclsDtlCdNm.equals("프리미엄 아울렛")) {
							processedName = "롯데 프리미엄 아울렛 " + cstrDspNm;
						} else if(lrclsDtlCdNm.equals("아울렛")) {
							processedName = "롯데아울렛 " + cstrDspNm;
						} else {
							processedName = cstrDspNm+" / "+mdclsDtlCdNm+" / "+lrclsDtlCdNm;
						}
						
						processedBrandchMap.get(cstrCd).put("data", processedName);
						
//						MallData md = new MallData();
//						md.setMallKey(cstrCd);
//						md.setMallName(processedName);
//						md.setMallType("롯데");
//						md.setMallUrl("https://www.lotteshopping.com/branchShopGuide/shopListAjax?_format=json&cstrCd=" + cstrCd);
//						md.setUpdatedTime(LocalDateTime.now());
//						mallRepository.upsert(md);
					}
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		return processedBrandchMap;
	}

	public Map<String, List<String>> collectFloorsByBranch(String cstrCd) {
//		String townUrl = "https://www.lotteshopping.com/branchShopGuide/shopListAjax?_format=json&cstrCd=" + cstrCd;
		// setting url
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("cstrCd", cstrCd);
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https").host("www.lotteshopping.com")
				.pathSegment("branchShopGuide", "shopListAjax").queryParams(params).build();
		String response = httpClient.get(uriComponents.toString(), null);
		
		Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
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
		return flrCdMap;
	}
}
