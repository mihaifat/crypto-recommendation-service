package com.crypto.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crypto.model.CryptoValue;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CryptoValueDAO {

    Logger logger = LoggerFactory.getLogger(CryptoValueDAO.class);

    public List<CryptoValue> getCryptoValuesByName(String name) {
        String[] headers = { "timestamp", "symbol", "price" };
        List<CryptoValue> cryptoValues = new ArrayList<>();
        String fileName = "src/main/resources/prices/" + name + "_values.csv";

        try (Reader in = new FileReader(fileName)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(headers)
                    .setSkipHeaderRecord(true)
                    .build();

            Iterable<CSVRecord> records = csvFormat.parse(in);

            for (CSVRecord csvRecord : records) {
                Date timestamp = new Date(Long.valueOf(csvRecord.get("timestamp")));
                String symbol = csvRecord.get("symbol");
                double price = Double.parseDouble(csvRecord.get("price"));
                cryptoValues.add(new CryptoValue(timestamp, symbol, price));
            }
        } catch (NumberFormatException | IOException e) {
            logger.error("Could not get crypto values by name: " + name + " due to following error", e);
        }

        return cryptoValues;
    }

}
