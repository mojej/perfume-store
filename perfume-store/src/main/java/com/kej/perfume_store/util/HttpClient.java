package com.kej.perfume_store.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class HttpClient {

	public String get(String url, Map<String, String> params) {
		String result = null;
		HttpGet request = new HttpGet(url);
		
		if(params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				request.addHeader(param.getKey(), param.getValue());
			}
		} else {
			// default 설정 - json
			request.addHeader("Content-Type", "application/json;charset=UTF-8");
		}

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
