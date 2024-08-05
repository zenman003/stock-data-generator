package com.satvik.stockpdfspringboot;

import com.satvik.stockpdfspringboot.Excel.StockController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockPdfSpringBootApplicationTests {

	@Autowired
	StockController stockController;

	@Test
	void contextLoads() {
		assert stockController != null;
	}

}
