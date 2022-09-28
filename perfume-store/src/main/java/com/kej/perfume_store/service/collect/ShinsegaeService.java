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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kej.perfume_store.model.MallData;
import com.kej.perfume_store.model.PerfumeStore;
import com.kej.perfume_store.repository.MallDataRepository;
import com.kej.perfume_store.repository.PerfumeStoreRepository;

@Service
public class ShinsegaeService {
	private Logger log = LoggerFactory.getLogger(ShinsegaeService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	@Qualifier("jdbcMallDataRepository") // Test JdbcTemplate
	private MallDataRepository mallDataRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository") // Test JdbcTemplate
	private PerfumeStoreRepository perfumeStoreRepository;
	
	
	public void collectStore() {
		List<MallData> mallList = mallDataRepository.findAll();
		for(MallData mall : mallList) {
			log.info("start collect: {}", mall.getMallName());
			Map<String, Object> stores = getStoresByMall(mall.getMallUrl());
			for(Object key : stores.keySet()) {
				// uk(store_key)로 존재 유무 확인
				// 존재하지 않는다면, insert
				PerfumeStore store = perfumeStoreRepository.findByUk(key.toString());
				if(store == null) {
					store = new PerfumeStore();
					store.setStoreKey(key.toString());
					store.setRawData(stores.get(key).toString());
					store.setMallId(mall.getMallId());
					perfumeStoreRepository.save(store);
					log.info("store inserted: {}", key);
				} 
				// 존재한다면, update
				else {
					store.setRawData(stores.get(key).toString());
					perfumeStoreRepository.update(store);
					log.info("store updated: {}", key);
				}
			}
			mall.setUpdatedTime(LocalDateTime.now());
			mallDataRepository.update(mall);
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


}
