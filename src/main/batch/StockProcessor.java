package com.satvik.stockpdfspringboot.batch;

import org.springframework.batch.item.ItemProcessor;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class StockProcessor implements ItemProcessor<StockLink, StockLink> {

    @Override
    public StockLink process(StockLink item) throws Exception {
        String url = item.getUrl();

        // Parse URL
        URI uri = new URI(url);
        String query = uri.getQuery();
        Map<String, String> queryPairs = Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param.length > 1 ? param[1] : ""));

        // Set URL-related fields
        item.setSymbol(getSymbolFromPath(uri.getPath()));
        item.setPeriod1(Long.parseLong(queryPairs.getOrDefault("period1", "0")));
        item.setPeriod2(Long.parseLong(queryPairs.getOrDefault("period2", "0")));
        item.setIntervalValue(queryPairs.getOrDefault("interval", ""));
        item.setIncludePrePost(Boolean.parseBoolean(queryPairs.getOrDefault("includePrePost", "false")));
        item.setEvents(queryPairs.getOrDefault("events", ""));
        item.setLang(queryPairs.getOrDefault("lang", ""));
        item.setRegion(queryPairs.getOrDefault("region", ""));
        item.setUserAgent(queryPairs.getOrDefault("user-agent", ""));

        // Parse and set headers
        parseAndSetHeaders(item);

        return item;
    }

    private String getSymbolFromPath(String path) {
        String[] pathSegments = path.split("/");
        int index = Arrays.asList(pathSegments).indexOf("chart");
        return (index != -1 && index + 1 < pathSegments.length) ? pathSegments[index + 1] : "";
    }

    private void parseAndSetHeaders(StockLink item) {
        // Assume we have a way to get the raw curl command string saved in the StockLink
        String curlCommand = item.getCurlCommand();

        // Extract headers from the curl command
        Map<String, String> headers = extractHeadersFromCurl(curlCommand);

        // Set the headers in the StockLink object
        item.setAccept(headers.getOrDefault("accept", ""));
        item.setAcceptLanguage(headers.getOrDefault("accept-language", ""));
        item.setCookie(headers.getOrDefault("cookie", ""));
        item.setOrigin(headers.getOrDefault("origin", ""));
        item.setPriority(headers.getOrDefault("priority", ""));
        item.setReferer(headers.getOrDefault("referer", ""));
        item.setSecChUa(headers.getOrDefault("sec-ch-ua", ""));
        item.setSecChUaMobile(headers.getOrDefault("sec-ch-ua-mobile", ""));
        item.setSecChUaPlatform(headers.getOrDefault("sec-ch-ua-platform", ""));
        item.setSecFetchDest(headers.getOrDefault("sec-fetch-dest", ""));
        item.setSecFetchMode(headers.getOrDefault("sec-fetch-mode", ""));
        item.setSecFetchSite(headers.getOrDefault("sec-fetch-site", ""));
        item.setUserAgent(headers.getOrDefault("user-agent", ""));
    }

    private Map<String, String> extractHeadersFromCurl(String curlCommand) {
        Map<String, String> headers = new HashMap<>();

        // Split the command into lines
        String[] lines = curlCommand.split("\\\\n");

        // Process each line to extract headers
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("-H '")) {
                String header = line.substring(4, line.length() - 1); // Remove -H ' and trailing '
                String[] parts = header.split(": ", 2);
                if (parts.length == 2) {
                    headers.put(parts[0].toLowerCase(), parts[1]);
                }
            }
        }

        return headers;
    }
}



// curl 'https://query2.finance.yahoo.com/v8/finance/chart/NVDA?period1=1718407200&period2=1718739000&interval=5m&includePrePost=true&events=div%7Csplit%7Cearn&&lang=en-US&region=US' \
//   -H 'accept: */*' \
//   -H 'accept-language: en-GB,en-US;q=0.9,en;q=0.8' \
//   -H 'cookie: A3=d=AQABBP1PaGYCEDYIOMNOknHVeTaB6RlvoPEFEgEBAQGhaWZyZlk6QDIB_eMAAA&S=AQAAAufDKuNN-llm7w9NSULMk0I; A1=d=AQABBP1PaGYCEDYIOMNOknHVeTaB6RlvoPEFEgEBAQGhaWZyZlk6QDIB_eMAAA&S=AQAAAufDKuNN-llm7w9NSULMk0I; axids=gam=y-HqoSNi1E2uJkuTZej_Os.l9uVBNuGVP.~A&dv360=eS1aSnUzcS5aRTJ1SDU1b01mbzdWMXVxUlpZSndObE9vMX5B&ydsp=y-Z6yi_WFE2uLli7rI75CJihDdrcu5QPxn~A&tbla=y-oLvcPlFE2uI3fSVeHCqViiqyaI.URyZH~A; tbla_id=3a8924d3-8c34-4a94-82dd-8807ba950adc-tuctd61d57d; A1S=d=AQABBP1PaGYCEDYIOMNOknHVeTaB6RlvoPEFEgEBAQGhaWZyZlk6QDIB_eMAAA&S=AQAAAufDKuNN-llm7w9NSULMk0I; gpp=DBAA; gpp_sid=-1; PRF=t%3DAAPL%252BNVDA; cmp=t=1718719995&j=0&u=1---; _chartbeat4=t=CVA1yBkR-9bBNSC-bChdHrwBg9rOB&E=12&x=0&c=775.13&y=7446&w=755' \
//   -H 'origin: https://finance.yahoo.com' \
//   -H 'priority: u=1, i' \
//   -H 'referer: https://finance.yahoo.com/quote/NVDA/' \
//   -H 'sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"' \
//   -H 'sec-ch-ua-mobile: ?0' \
//   -H 'sec-ch-ua-platform: "macOS"' \
//   -H 'sec-fetch-dest: empty' \
//   -H 'sec-fetch-mode: cors' \
//   -H 'sec-fetch-site: same-site' \
//   -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36'
