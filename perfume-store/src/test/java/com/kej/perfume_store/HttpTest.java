package com.kej.perfume_store;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class HttpTest {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void htmlGet() throws IOException {
		String url = "https://www.shinsegae.com/store/floor.do?storeCd=SC00001";
	    HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", "text/html; charset=utf-8");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
//            System.out.println(response.getStatusLine().getStatusCode());   // 200
//            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
//            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                
                // without curly braces - var arrFloor = \\{(.*?)\\};
                Pattern pattern = Pattern.compile("var arrFloor = ([\\{].+?[\\}]);");
                Matcher matcher = pattern.matcher(result);
                
                Map<String, Object> perfumes = new HashMap<>();
                if(matcher.find()) {
                	Map<String, Object> map = objectMapper.readValue(matcher.group(1), new TypeReference<Map<String,Object>>(){});
                	if(map.get("floor")!=null) {
                		List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("floor");
                		for(Map<String, Object> floor : list) {
                			// category[] > brand[] >  "sh00026" 값이 퍼퓸이면 수집
                			if(floor.get("category") != null) {
                				List<Map<String, Object>> categories = (List<Map<String, Object>>) floor.get("category");
                				
                				for(Map<String, Object> category:categories) {
                					if(category.get("brand") != null) {
                						List<Map<String, Object>> brands = (List<Map<String, Object>>) category.get("brand");
                						
                						for(Map<String,Object> brand:brands) {
                							if(brand.get("ub00008")!=null && brand.get("ub00008").toString().indexOf("화장품") != -1) {
                								perfumes.put(brand.get("shop_id").toString(), brand);
                							}
//                							if(brand.get("sh00026")!=null && 
//                									(brand.get("sh00026").toString().indexOf("퍼퓸") != -1 || brand.get("sh00026").toString().indexOf("향수") != -1) ) {
//                								perfumes.put(brand.get("shop_id").toString(), brand);
//                							}
                						} // brand
                					}
                				} // category
                				
                			}
                		} // floor
                	}
//                	System.out.println(perfumes);
                	String aaa = objectMapper.writeValueAsString(perfumes);
                	System.out.println(aaa);
                }
                
            }
            
        }
		
	}
	
}
