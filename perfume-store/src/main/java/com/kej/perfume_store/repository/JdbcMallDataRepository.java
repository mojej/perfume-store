package com.kej.perfume_store.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kej.perfume_store.model.MallData;
import com.kej.perfume_store.model.PerfumeStore;

@Repository
public class JdbcMallDataRepository implements MallDataRepository {

	@Autowired  private JdbcTemplate jdbcTemplate;

	@Override
	public int update(MallData mallData) {
		String sql = "update perfume.mall_data set updated_time = ? where mall_id = ?";
		return jdbcTemplate.update(sql, mallData.getUpdatedTime(), mallData.getMallId());
	}

	@Override
	public List<MallData> findAll() {
		String sql = "select * from perfume.mall_data";
		return jdbcTemplate.query(sql, 
				(resultSet, rowNum) -> 
					new MallData(
						resultSet.getInt("mall_id"),
						resultSet.getString("mall_key"),
						resultSet.getString("mall_name"),
						resultSet.getString("mall_type"),
						resultSet.getString("mall_url"),
						resultSet.getTimestamp("updated_time") == null ? null : resultSet.getTimestamp("updated_time").toLocalDateTime()
					)
				);
	}
	
}
