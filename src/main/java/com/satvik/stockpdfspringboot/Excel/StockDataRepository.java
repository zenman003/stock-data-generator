package com.satvik.stockpdfspringboot.Excel;

import com.satvik.stockpdfspringboot.Excel.dto.StockDataDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDataRepository extends JpaRepository<StockDataDto, Long> {
}
