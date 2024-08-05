package com.satvik.stockpdfspringboot.Excel;

import com.satvik.stockpdfspringboot.Excel.dto.StockDataDto;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static java.lang.System.out;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final UserRepository userRepository;

    public ByteArrayInputStream generateExcel(Long userId, String sheetName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Set<UserStock> userStocks = user.getUserStocks();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = createSheet(workbook, sheetName);
            int rowNum = 1;
            for (UserStock userStock : userStocks) {
                String symbol = userStock.getSymbol();
                StockDataDto stockDataDto = getStockDataInternal(symbol);
                populateSheetWithStockData(sheet, stockDataDto, rowNum);
                rowNum++;
            }
            try {
                workbook.write(outputStream);
            } catch (IOException e) {
                System.out.println("Error writing Excel file: " + e.getMessage());
            }

        } catch (IOException e) {
            out.println("Error creating Excel workbook: " + e.getMessage());
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private Sheet createSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        for (int i = 0; i <= 11; i++) {
            sheet.setColumnWidth(i, 6000);
        }
        createHeaderRow(sheet);
        return sheet;
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = {"Stock Name", "Currency", "Regular Market Price", "Regular Market Day High", "Regular Market Day Low", "52 Week High", "52 Week Low"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void populateSheetWithStockData(Sheet sheet, StockDataDto stockDataDto, int rowNum) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(stockDataDto.getStockName());
        row.createCell(1).setCellValue(stockDataDto.getCurrency());
        row.createCell(2).setCellValue(stockDataDto.getRegularMarketPrice());
        row.createCell(3).setCellValue(stockDataDto.getRegularMarketDayHigh());
        row.createCell(4).setCellValue(stockDataDto.getRegularMarketDayLow());
        row.createCell(5).setCellValue(stockDataDto.getFiftyTwoWeekHigh());
        row.createCell(6).setCellValue(stockDataDto.getFiftyTwoWeekLow());

    }

    private StockDataDto getStockDataInternal(String symbol) {
        StockDataDto stockDataDto = new StockDataDto();
        Instant instant = Instant.now();
        long period1 = instant.getEpochSecond() - 31536000; // 1 year ago
        long period2 = instant.getEpochSecond(); // current time
        String interval = "1d";
        String url = "https://query2.finance.yahoo.com/v8/finance/chart/" + symbol +
                "?period1=" + period1 + "&period2=" + period2 +
                "&interval=" + interval + "&includePrePost=true&events=div%7Csplit%7Cearn&lang=en-US&region=US";


        HttpGet request = new HttpGet(url);
        request.addHeader("accept", "*/*");
        request.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request);

             BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {


            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            parseStockData(sb.toString(), stockDataDto);
        } catch (IOException e) {
            out.println("Error getting stock data: " + e.getMessage());
        }
        return stockDataDto;
    }

    private void parseStockData(String jsonResponse, StockDataDto stockDataDto) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Check if "chart" object exists
        if (jsonObject.has("chart")) {
            JSONObject chart = jsonObject.getJSONObject("chart");

            // Check if "result" array exists and is not null
            if (chart.has("result") && !chart.isNull("result")) {
                JSONArray result = chart.getJSONArray("result");

                // Ensure there is at least one item in the "result" array
                if (!result.isEmpty()) {
                    JSONObject meta = result.getJSONObject(0).getJSONObject("meta");

                    stockDataDto.setStockName(meta.getString("symbol"));
                    stockDataDto.setRegularMarketPrice(meta.getBigDecimal("regularMarketPrice").toString());
                    stockDataDto.setRegularMarketDayHigh(meta.getBigDecimal("regularMarketDayHigh").toString());
                    stockDataDto.setRegularMarketDayLow(meta.getBigDecimal("regularMarketDayLow").toString());
                    stockDataDto.setCurrency(meta.getString("currency"));

                    // Initialize 52-week high and low
                    BigDecimal fiftyTwoWeekHigh = BigDecimal.ZERO;
                    BigDecimal fiftyTwoWeekLow = new BigDecimal("999999");

                    // Check if "indicators" object exists
                    if (result.getJSONObject(0).has("indicators")) {
                        JSONObject indicators = result.getJSONObject(0).getJSONObject("indicators");

                        // Check if "quote" array exists and is not null
                        if (indicators.has("quote") && !indicators.isNull("quote")) {
                            JSONArray quoteArray = indicators.getJSONArray("quote");

                            // Ensure there is at least one item in the "quote" array
                            if (!quoteArray.isEmpty()) {
                                JSONObject parameters = quoteArray.getJSONObject(0);

                                // Set values only if the fields exist
                                if (parameters.has("high") && !parameters.isNull("high")) {
                                    JSONArray highArray = parameters.getJSONArray("high");
                                    for (int i = 0; i < highArray.length(); i++) {
                                        BigDecimal highValue = highArray.optBigDecimal(i, BigDecimal.ZERO);
                                        if (highValue.compareTo(fiftyTwoWeekHigh) > 0) {
                                            fiftyTwoWeekHigh = highValue;
                                        }
                                    }
                                    stockDataDto.setHigh(fiftyTwoWeekHigh.toString());
                                }
                                if (parameters.has("low") && !parameters.isNull("low")) {
                                    JSONArray lowArray = parameters.getJSONArray("low");
                                    for (int i = 0; i < lowArray.length(); i++) {
                                        BigDecimal lowValue = lowArray.optBigDecimal(i, BigDecimal.ZERO);
                                        if (lowValue.compareTo(fiftyTwoWeekLow) < 0) {
                                            fiftyTwoWeekLow = lowValue;
                                        }
                                    }
                                    stockDataDto.setLow(fiftyTwoWeekLow.toString());
                                }
                            }
                        }
                    }

                    // Set 52-week high and low in DTO
                    stockDataDto.setFiftyTwoWeekHigh(fiftyTwoWeekHigh.toString());
                    stockDataDto.setFiftyTwoWeekLow(fiftyTwoWeekLow.toString());
                }
            }
        }
    }
}
