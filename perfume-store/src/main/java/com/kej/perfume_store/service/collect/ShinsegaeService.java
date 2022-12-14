package com.kej.perfume_store.service.collect;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kej.perfume_store.model.MallData;
import com.kej.perfume_store.model.PerfumeStore;
import com.kej.perfume_store.repository.MallDataRepository;
import com.kej.perfume_store.repository.PerfumeStoreRepository;
import com.kej.perfume_store.service.BrandService;

@Service
public class ShinsegaeService {
	private Logger log = LoggerFactory.getLogger(ShinsegaeService.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	Map<String, Integer> brandMap = null;
	
	@Autowired
	@Qualifier("jdbcMallDataRepository") // Test JdbcTemplate
	private MallDataRepository mallRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository") // Test JdbcTemplate
	private PerfumeStoreRepository storeRepository;
	
	@Autowired BrandService brandService; 
	
	public void collectStore() {
		brandMap = brandService.getBrandMappingMap();
		
		List<MallData> mallList = mallRepository.findByType("신세계백화점");
		for(MallData mall : mallList) {
			log.info("start collect: {}", mall.getMallName());
			Map<String, Object> stores = getStoresByMall(mall.getMallUrl());
			for(Object key : stores.keySet()) {
				PerfumeStore ps = new PerfumeStore();
				ps.setStoreKey(key.toString());
				ps.setMallId(mall.getMallId());
				try {
					String rawdata = objectMapper.writeValueAsString(stores.get(key));
					ps.setRawData(rawdata);
					Map<String, Object> map = objectMapper.readValue(rawdata, new TypeReference<Map<String,Object>>() {});
					String brandName = map.get("shop_nm").toString();
					String tel = map.get("sh00009").toString();
					
					Integer brandId = brandMap.get(brandName);
					if(brandId == null) continue;
					
					ps.setBrandName(brandName);
					ps.setPhoneNumber(tel);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				storeRepository.upsert(ps);
				log.info("store upserted: {}", key);
			}
			mall.setUpdatedTime(LocalDateTime.now());
			mallRepository.update(mall);
			log.info("finish collect: {}", mall.getMallName());
		}
	}
	
	private Map<String, Object> getStoresByMall(String mallUrl) {
	    HttpGet request = new HttpGet(mallUrl);
        request.addHeader("Content-Type", "text/html; charset=utf-8");

        Map<String, Object> perfumes = new HashMap<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                
                Pattern pattern = Pattern.compile("var arrFloor = ([\\{].+?[\\}]);");
                Matcher matcher = pattern.matcher(result);
                
                if(matcher.find()) {
                	Map<String, Object> map = objectMapper.readValue(matcher.group(1), new TypeReference<Map<String,Object>>(){});
                	if(map.get("floor")!=null) {
                		List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("floor");
                		Integer currentFloor = 1;
                		for(Map<String, Object> floor : list) {
                			log.info("current floor: " + currentFloor + "/" + list.size());
                			currentFloor++;
                			// category[] > brand[] >  "sh00026" 값이 퍼퓸이면 수집
                			if(floor.get("category") != null) {
                				List<Map<String, Object>> categories = (List<Map<String, Object>>) floor.get("category");
                				
                				for(Map<String, Object> category:categories) {
                					if(category.get("brand") != null) {
                						List<Map<String, Object>> brands = (List<Map<String, Object>>) category.get("brand");
                						
                						for(Map<String,Object> brand:brands) {
                							if(brand.get("sh00026")!=null && 
                									(brand.get("sh00026").toString().indexOf("퍼퓸") != -1 || brand.get("sh00026").toString().indexOf("향수") != -1) ) {
                								perfumes.put(brand.get("shop_id").toString(), brand);
                							}
                						} // brand
                					}
                				} // category
                				
                			}
                		} // floor
                	}
                }
                
            }
        } catch (ParseException e) {
        	log.error("parseing error: {}", e);
		} catch (IOException e) {
			log.error("IOException error: {}", e);
		}
        return perfumes;
	}

	public void mappingStoreToBrand() {
		Map<String, Integer> brands = brandService.getBrandMappingMap();
		
		List<PerfumeStore> stores = storeRepository.findAll();
		for(PerfumeStore store:stores) {
			String json = store.getRawData();
			try {
				Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>() {});
				String name = map.get("shop_nm").toString();
				if(brands.get(name) != null) {
					System.out.println(name +":"+ brands.get(name));
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
	}

}
