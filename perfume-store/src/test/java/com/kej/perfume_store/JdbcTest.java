package com.kej.perfume_store;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.kej.perfume_store.model.MallData;
import com.kej.perfume_store.model.PerfumeBrand;
import com.kej.perfume_store.model.PerfumeStore;
import com.kej.perfume_store.repository.MallDataRepository;
import com.kej.perfume_store.repository.PerfumeBrandRepository;
import com.kej.perfume_store.repository.PerfumeStoreRepository;

@SpringBootTest
public class JdbcTest {

	@Autowired
	@Qualifier("jdbcMallDataRepository") // Test JdbcTemplate
	private MallDataRepository mallDataRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository") // Test JdbcTemplate
	private PerfumeStoreRepository perfumeStoreRepository;
	@Autowired
	@Qualifier("jdbcPerfumeBrandRepository") // Test JdbcTemplate
	private PerfumeBrandRepository perfumeBrandRepository;

	@Test
	public void springJdbcTest() {
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		List<MallData> list = mallDataRepository.findAll();
		for (MallData data : list) {
			System.out.println(data.getMallId() + ": " + data.getMallName());
		}
	}
	
	@Test
	public void findByUk() {
		PerfumeStore ps = perfumeStoreRepository.findByUk("1234");
		System.out.println(ps != null ? ps.getStoreId() : "null!");
	}
	
	
	
	/**
	 * perfume brand
	 */
	
	@Test
	public void updateBrand() {
		PerfumeBrand pb = perfumeBrandRepository.findById(1);
		System.out.println(pb.getProcessedName() +": "+ pb.getCollectedName());
//		perfumeBrandRepository.update(brand);
	}

}
