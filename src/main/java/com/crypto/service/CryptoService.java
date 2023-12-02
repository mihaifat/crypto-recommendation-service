package com.crypto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crypto.dto.CryptoStatsDTO;
import com.crypto.dto.NormalizedRangeDTO;
import com.crypto.model.CryptoValue;
import com.crypto.model.NormalizedRange;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CryptoService {

    Logger logger = LoggerFactory.getLogger(CryptoService.class);

    private final CryptoValueDAO cryptoValueDAO;
    private final CryptoStatsCalculator cryptoStatsCalculator;

    public CryptoStatsDTO getCryptoStats(String name) {

        List<CryptoValue> cryptoValues = cryptoValueDAO.getCryptoValuesByName(name);

        CryptoValue oldestCryptoValue = cryptoStatsCalculator.getOldest(cryptoValues);
        CryptoValue newestCryptoValue = cryptoStatsCalculator.getNewest(cryptoValues);
        CryptoValue minCryptoValue = cryptoStatsCalculator.getMin(cryptoValues);
        CryptoValue maxCryptoValue = cryptoStatsCalculator.getMax(cryptoValues);

        CryptoStatsDTO stats = new CryptoStatsDTO();

        stats.setOldestValue(oldestCryptoValue.getPrice());
        stats.setNewestValue(newestCryptoValue.getPrice());
        stats.setMinValue(minCryptoValue.getPrice());
        stats.setMaxValue(maxCryptoValue.getPrice());

        return stats;
    }

    public List<NormalizedRangeDTO> getNormalizedRanges() {
        List<String> cryptoNames = getCryptoNamesFromResourceFiles();
        return cryptoNames.stream().map(name -> {
            List<CryptoValue> cryptoValues = cryptoValueDAO.getCryptoValuesByName(name);
            NormalizedRange normalizedRange = cryptoStatsCalculator.getNormalizedRange(name, cryptoValues);
            return new NormalizedRangeDTO(normalizedRange.getName(), normalizedRange.getRange());
        })
        .sorted(Comparator.comparing(NormalizedRangeDTO::getRange).reversed())
        .collect(Collectors.toList());
    }

    public List<String> getCryptoNamesFromResourceFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/prices"))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString().replace("_values.csv", ""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Could get normalized ranges due to following error", e);
        }
        return Collections.emptyList();
    }
}
