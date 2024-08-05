package com.satvik.stockpdfspringboot.Excel;

import com.satvik.stockpdfspringboot.Excel.dto.AddStockDto;
import com.satvik.stockpdfspringboot.Excel.dto.ExcelRequestDto;
import com.satvik.stockpdfspringboot.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final UserService userService;
    private final ExcelService excelService;
    private final UserStockService userStockService;

    @PostMapping("/user/add_stock")
    public ResponseEntity<UserStock> addStockToUser(@RequestBody AddStockDto request) {
        try {
            UserStock userStock = userStockService.addStockToUser(request.getUsername(), request.getSymbol());
            return new ResponseEntity<>(userStock, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/genexcel")
    public ResponseEntity<byte[]> generateExcel(@RequestBody ExcelRequestDto request) {
        try {
            String userName = request.getUsername();
            String sheetName = request.getSheetName();
            if (sheetName == null || sheetName.isEmpty()) {
                sheetName = "Stock Data";
            }

            Long userId = userService.findByUsername(userName).getId();
            ByteArrayInputStream excelStream = excelService.generateExcel(userId, sheetName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock-data.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            byte[] excelBytes = excelStream.readAllBytes();
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("admin/getUsers")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}
