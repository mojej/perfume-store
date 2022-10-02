package com.kej.perfume_store.service.collect;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kej.perfume_store.model.MallData;
import com.kej.perfume_store.model.PerfumeStore;
import com.kej.perfume_store.repository.MallDataRepository;
import com.kej.perfume_store.repository.PerfumeStoreRepository;
import com.kej.perfume_store.service.BrandService;

@Service
public class HyundaiService {

	private Logger log = LoggerFactory.getLogger(HyundaiService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	@Qualifier("jdbcMallDataRepository") // Test JdbcTemplate
	private MallDataRepository mallRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository") // Test JdbcTemplate
	private PerfumeStoreRepository storeRepository;
	@Autowired BrandService brandService;
	Map<String, Integer> brandMap = null;
	
	public void collectStore() {
		brandMap = brandService.getBrandMappingMap();
		// get mall list
		List<MallData> malls = mallRepository.findByType("현대백화점");
		for (MallData mall : malls) {
			String html = httpClient(mall.getMallUrl());
			int upserted = setPerfumeStore(html, mall.getMallId());
			log.info(mall.getMallName() + " upserted: {}", upserted);
			mall.setUpdatedTime(LocalDateTime.now());
			mallRepository.update(mall);
		}
	}
	
	public int setPerfumeStore(String html, Integer mallId) {
		int count = 0;
		Document doc = Jsoup.parse(html);
		Elements sections = doc.select(".tab_contgroup > .section");
		for(Element section : sections) {
			String sectionName = section.select("h5").text();
			if(sectionName.equals("화장품") || sectionName.equals("향수")) {
				Elements stores = section.select(".itemgroup > .item");
				for(Element store : stores) {

					String storeName = store.selectFirst("strong").text();
					Integer brandId = brandMap.get(storeName);
					// 브랜드가 매칭 된다면 저장
					if(brandId != null) {
						String key = store.select("input") == null? null: store.select("input").val();
						String tel = store.select("span.tel") == null ? null: store.select("span.tel").text();
						
						PerfumeStore ps = new PerfumeStore();
						ps.setStoreKey(key);
						ps.setBrandName(storeName);
						ps.setRawData(store.html());
						ps.setPhoneNumber(tel);
						ps.setMallId(mallId);
						count += storeRepository.upsert(ps);
						log.info(ps.getBrandName() +" upserted..");
					}
					
				}
			}
		}
		return count;
	}
	


	private String httpClient(String url) {
		String result = null;
		HttpGet request = new HttpGet(url);
		request.addHeader("Content-Type", "text/html; charset=utf-8");

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(request)) {

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			log.error("IOException error: {}", e);
		}
		return result;
	}

}
