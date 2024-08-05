package com.satvik.stockpdfspringboot.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class RowMapperImpl implements RowMapper<StockLink> {
    private static final Logger logger = LoggerFactory.getLogger(RowMapperImpl.class);

    public RowMapperImpl() {
    }

    @Override
    public StockLink mapRow(RowSet rs) throws Exception {
        if (rs == null || rs.getCurrentRow() == null) {
            return null;
        }

        StockLink bl = new StockLink();
        try {
            String stockUrl = rs.getCurrentRow()[0];
            if (stockUrl != null) {
                bl.setUrl(stockUrl);
            } else {
                logger.warn("Stock URL is null for row: {}", rs.getCurrentRow());
            }
        } catch (Exception e) {
            logger.error("Error mapping row: {}", rs.getCurrentRow(), e);
            throw e;
        }

        return bl;
    }
}
