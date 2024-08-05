package com.satvik.stockpdfspringboot.Excel.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class ExcelRequestDto {
    String username;

    @Null
    String sheetName;
}
