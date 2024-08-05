package com.satvik.stockpdfspringboot.Excel.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stock_data")
public class StockDataDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId; // Use Long instead of String for ID generation

    private String stockName;
    private String high;
    private String low;
    private String open;
    private String close;
    private String volume;
    private String currency;
    private String regularMarketDayHigh;
    private String regularMarketDayLow;
    private String fiftyTwoWeekHigh;
    private String fiftyTwoWeekLow;
    private String regularMarketPrice;

}