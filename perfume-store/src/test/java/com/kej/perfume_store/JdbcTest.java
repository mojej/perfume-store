package com.kej.perfume_store;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	@Qualifier("jdbcMallDataRepository")
	private MallDataRepository mallDataRepository;
	@Autowired
	@Qualifier("jdbcPerfumeStoreRepository")
	private PerfumeStoreRepository perfumeStoreRepository;
	@Autowired
	@Qualifier("jdbcPerfumeBrandRepository")
	private PerfumeBrandRepository perfumeBrandRepository;

	
	/**
	 * mall_data
	 */
	
	@Test
	public void getMallDataAndUpdate() {
		Integer mallId = 1;
		MallData mallData = mallDataRepository.findById(mallId);
		mallData.setUpdatedTime(LocalDateTime.now());
		int update = mallDataRepository.update(mallData);
		System.out.println(mallData.getMallName()+" updated: "+update );
	}
	
	
	@Test
	public void mallDataFindAll() {
		List<MallData> list = mallDataRepository.findAll();
		for (MallData data : list) {
			System.out.println(data.getMallId() + ": " + data.getMallName());
		}
	}
	
	@Test
	public void getMallByType() {
		String mallType = "현대백화점";
		List<MallData> list = mallDataRepository.findByType(mallType);
		for(MallData mall:list) {
			System.out.println(mall.getMallId() +": "+mall.getMallName());
		}
	}
	
	
	
	/**
	 * perfume_store
	 * 
	 */
	@Test
	public void findByUk() {
		PerfumeStore ps = perfumeStoreRepository.findByUk("9090000104");
		System.out.println(ps != null ? ps.getStoreId() : "null!");
	}
	
	@Test
	public void findByName() {
		String storeName = "";
		List<PerfumeStore> stores = perfumeStoreRepository.findByName(storeName);
		System.out.println("stores length: "+stores.size());
		for(PerfumeStore store:stores) {
			System.out.println(store.getStoreId() +": "+ store.getBrandName());
		}
	}
	
	@Test
	public void upsertStore() {
		PerfumeStore store = new PerfumeStore();
		store.setBrandName("ej store2222222222");
		store.setStoreKey("ejejej");
		store.setPhoneNumber("010-9923-1625");
		store.setMallId(1);
		int save = perfumeStoreRepository.upsert(store);
		System.out.println("saved store: " +save);
	}
	
	@Test
	public void getStoreAndUpdate() {
		Integer storeId = 98;
		PerfumeStore store = perfumeStoreRepository.findById(storeId);
		store.setPhoneNumber("010-9923-1625");
		int update = perfumeStoreRepository.update(store);
		System.out.println(store.getBrandName() +" updated: "+ update);
	}
	
	@Test
	public void getAllStores() {
		List<PerfumeStore> listAll = perfumeStoreRepository.findAll();
		for(PerfumeStore store:listAll) {
			System.out.println(store.getStoreId() +": "+ store.getBrandName());
		}
	}
	
	
	
	/**
	 * perfume brand
	 */
	@Test
	public void updateBrand() {
		PerfumeBrand pb = perfumeBrandRepository.findById(1);
		List<Integer> ids = new ArrayList<>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		pb.setStoreId(ids);
		System.out.println(pb.getProcessedName()+", "+pb.getCollectedName()+", "+pb.getStoreId());
		int update = perfumeBrandRepository.update(pb);
		System.out.println(pb.getProcessedName() +" updated ids :" + update);
	}
	
	@Test
	public void getAllBrands() {
		List<PerfumeBrand> list = perfumeBrandRepository.findAll();
		for(PerfumeBrand brand:list) {
			System.out.println(brand.getBrandId()+": "+brand.getProcessedName());
		}
	}
	
}
