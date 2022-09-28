package com.kej.perfume_store.repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kej.perfume_store.mapper.PerfumeBrandMapper;
import com.kej.perfume_store.model.PerfumeBrand;

@Repository
public class JdbcPerfumeBrandRepository implements PerfumeBrandRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int update(PerfumeBrand brand) {
		String sql = "update perfume.perfume_brand set collected_name=?, processed_name=?, store_id=? where brand_id=?";
		return jdbcTemplate.update(sql, 
				createSqlStringArray(brand.getCollectedName()), 
				brand.getProcessedName(), 
				createSqlIntegerArray(brand.getStoreId()), 
				brand.getBrandId()
		);
	}

	@Override
	public List<PerfumeBrand> findAll() {
		String sql = "select * from perfume.perfume_brand";
		
		// https://stackoverflow.com/questions/73358419/how-can-i-deserialize-row-to-dto
		return jdbcTemplate.query(
				sql, 
				new PerfumeBrandMapper());
	}

	@Override
	public PerfumeBrand findById(Integer brandId) {
		String sql = "select * from perfume.perfume_brand where brand_id=?";
		try {
			return jdbcTemplate.queryForObject(sql, new PerfumeBrandMapper(), brandId);
		} catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private Array createSqlIntegerArray(List<Integer> list){
	    Array intArray = null;
	    try {
	        intArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("INTEGER", list.toArray());
	    } catch (SQLException ignore) {
	    }
	    return intArray;
	}
	private Array createSqlStringArray(List<String> list){
		Array strArray = null;
		try {
			strArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("VARCHAR", list.toArray());
		} catch (SQLException ignore) {
		}
		return strArray;
	}

}
