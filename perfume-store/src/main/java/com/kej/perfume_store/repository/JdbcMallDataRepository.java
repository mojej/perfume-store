package com.kej.perfume_store.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kej.perfume_store.model.MallData;

@Repository
public class JdbcMallDataRepository implements MallDataRepository {

	@Autowired  private JdbcTemplate jdbcTemplate;

	@Override
	public int upsert(MallData mallData) {
		String sql = "insert into perfume.mall_data (mall_name, mall_type, mall_url, mall_key, updated_time) values(?,?,?,?,?)"
				+"ON CONFLICT(mall_key) do update set mall_name=?, mall_type=?, mall_url=?, mall_key=?, updated_time=?";
		return jdbcTemplate.update(sql, 
			mallData.getMallName(), mallData.getMallType(), mallData.getMallUrl(), mallData.getMallKey(), mallData.getUpdatedTime(),
			mallData.getMallName(), mallData.getMallType(), mallData.getMallUrl(), mallData.getMallKey(), mallData.getUpdatedTime());
	}
	
	@Override
	public MallData findById(Integer mallId) {
		String sql = "select * from perfume.mall_data where mall_id=?";
		try {	
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<MallData>(MallData.class), mallId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public int update(MallData mallData) {
		String sql = "update perfume.mall_data set updated_time = ? where mall_id = ?";
		return jdbcTemplate.update(sql, mallData.getUpdatedTime(), mallData.getMallId());
	}

	@Override
	public List<MallData> findAll() {
		String sql = "select * from perfume.mall_data";
		return jdbcTemplate.query(
					sql, 
					new BeanPropertyRowMapper<MallData>(MallData.class)
				);
	}

	@Override
	public List<MallData> findByType(String mallType) {
		String sql = "select * from perfume.mall_data where mall_type=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MallData>(MallData.class), mallType);
	}
	
}
