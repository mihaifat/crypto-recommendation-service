package com.crypto.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.crypto.model.CryptoValue;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CryptoValueDAO {

    private static final Logger logger = LoggerFactory.getLogger(CryptoValueDAO.class);

    private static final String[] HEADERS = { "timestamp", "symbol", "price" };

    public List<CryptoValue> getCryptoValuesByName(String name) {
        try {
            Stream<CSVRecord> records = getCSVRecords(name);
            return records.map(this::csvRecordToCryptoValue).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Could not get crypto values by name: " + name + " due to following error", e);
        }
        return Collections.emptyList();
    }

    public List<CryptoValue> getCryptoValuesByNameAndDateTimestamp(String name, String dateTimestamp) {
        try {
            LocalDate date = LocalDate.parse(dateTimestamp);
            
            LocalDateTime localDateTimeStart = date.atStartOfDay();
            Instant instantStart = localDateTimeStart.toInstant(ZoneOffset.UTC);
            long unixTimestampStart = instantStart.getEpochSecond() * 1000L;

            LocalDateTime localDateTimeEnd = date.atTime(23, 59, 59);
            Instant instantEnd = localDateTimeEnd.toInstant(ZoneOffset.UTC);
            long unixTimestampEnd = instantEnd.getEpochSecond() * 1000L;
            
            Stream<CSVRecord> records = getCSVRecords(name);
            return records
                    .map(this::csvRecordToCryptoValue)
                    .filter(cryptoValue -> cryptoValue.getTimestamp() >= unixTimestampStart && cryptoValue.getTimestamp() <= unixTimestampEnd)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Could not get crypto values by name: " + name + " and dateTimestamp:" + dateTimestamp
                    + " due to following error", e);
        }
        return Collections.emptyList();
    }

    private CryptoValue csvRecordToCryptoValue(CSVRecord csvRecord) {
        long timestamp = Long.parseLong(csvRecord.get("timestamp"));
        String symbol = csvRecord.get("symbol");
        double price = Double.parseDouble(csvRecord.get("price"));
        return new CryptoValue(timestamp, symbol, price);
    }

    private Stream<CSVRecord> getCSVRecords(String name) throws IOException {
        String resourcePath = "prices/" + name + "_values.csv";
        try {
            InputStream is = new ClassPathResource(resourcePath).getInputStream();
            Reader in = new InputStreamReader(is);
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> iterable = csvFormat.parse(in);
            return StreamSupport.stream(iterable.spliterator(), false);
        } catch (IOException e) {
            logger.error("Could not get CSV records from resourcePath: " + resourcePath + " due to following error", e);
            throw e;
        }
    }

}
