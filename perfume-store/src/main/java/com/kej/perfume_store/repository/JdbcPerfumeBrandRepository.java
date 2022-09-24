package com.kej.perfume_store.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kej.perfume_store.model.PerfumeBrand;

@Repository
public class JdbcPerfumeBrandRepository implements PerfumeBrandRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int update(PerfumeBrand brand) {
		String sql = "update perfume.perfume_brand set collected_name=?, processed_name=?, store_id=? where brand_id=?";
		return jdbcTemplate.update(sql, 
			brand.getCollectedName(), brand.getProcessedName(), brand.getStoreId(), brand.getBrandId());
	}

	@Override
	public List<PerfumeBrand> findAll() {
		String sql = "select * from perfume.perfume_brand";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PerfumeBrand>(PerfumeBrand.class));
	}

	@Override
	public PerfumeBrand findById(Integer brandId) {
		String sql = "select * from perfume.perfume_brand where brand_id=?";
		try {
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<PerfumeBrand>(PerfumeBrand.class), brandId);
		} catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

}
