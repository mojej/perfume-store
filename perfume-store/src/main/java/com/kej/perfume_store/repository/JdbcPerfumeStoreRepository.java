package com.kej.perfume_store.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kej.perfume_store.model.PerfumeStore;

@Repository
public class JdbcPerfumeStoreRepository implements PerfumeStoreRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int save(PerfumeStore store) {
		String sql = "insert into perfume.perfume_store (brand_name, phone_number, raw_data, mall_id, store_key) values (?,?,?,?,?)";
		return jdbcTemplate.update(sql, store.getBrandName(), store.getPhoneNumber(), store.getRawData(),
				store.getMallId(), store.getStoreKey());
	}

	@Override
	public int update(PerfumeStore store) {
		String sql = "update perfume.perfume_store set brand_name=?, phone_number=?, raw_data=?, mall_id=?, store_key=? where store_id=?";
		return jdbcTemplate.update(sql, store.getBrandName(), store.getPhoneNumber(), store.getRawData(),
				store.getMallId(), store.getStoreKey(), store.getMallId());
	}

	@Override
	public List<PerfumeStore> findAll() {
		String sql = "select * from perfume.perfume_store";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PerfumeStore>(PerfumeStore.class));
	}

	@Override
	public List<PerfumeStore> findByName(String brandName) {
		String sql = "select * from perfume.perfume_store where brand_name=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PerfumeStore>(PerfumeStore.class), brandName);
	}

	@Override
	public int countByUk(String storeKey) {
		String sql = "select * from perfume.perfume_store where store_key=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, storeKey);
	}

	@Override
	public PerfumeStore findByUk(String storeKey) {
		String sql = "select * from perfume.perfume_store where store_key=?";
		try {	
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<PerfumeStore>(PerfumeStore.class), storeKey);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
