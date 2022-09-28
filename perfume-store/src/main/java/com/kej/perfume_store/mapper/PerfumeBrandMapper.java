package com.kej.perfume_store.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.jdbc.core.RowMapper;

import com.kej.perfume_store.model.PerfumeBrand;

public class PerfumeBrandMapper implements RowMapper<PerfumeBrand> {

	@Override
	public PerfumeBrand mapRow(ResultSet rs, int rowNum) throws SQLException {
		PerfumeBrand brand = new PerfumeBrand();
		brand.setBrandId(rs.getInt("brand_id"));
		brand.setProcessedName(rs.getString("processed_name"));
		
		if(rs.getArray("collected_name") != null) {
			String[] names = (String[])rs.getArray("collected_name").getArray();
			Collections.addAll(brand.getCollectedName(), names);
		}

		if(rs.getArray("store_id") != null) {
			Integer[] ids = (Integer[])rs.getArray("store_id").getArray();
			Collections.addAll(brand.getStoreId(), ids);
		}
		
		return brand;
	}
	

}
