package com.crypto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.crypto.dto.CryptoStatsDTO;
import com.crypto.dto.NormalizedRangeDTO;
import com.crypto.model.CryptoValue;
import com.crypto.model.NormalizedRange;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);

    private final CryptoValueDAO cryptoValueDAO;
    private final CryptoStatsCalculator cryptoStatsCalculator;

    public CryptoStatsDTO getCryptoStats(String name) {

        List<CryptoValue> cryptoValues = cryptoValueDAO.getCryptoValuesByName(name);

        Optional<CryptoValue> oldestCryptoValue = cryptoStatsCalculator.getOldest(cryptoValues);
        Optional<CryptoValue> newestCryptoValue = cryptoStatsCalculator.getNewest(cryptoValues);
        Optional<CryptoValue> minCryptoValue = cryptoStatsCalculator.getMin(cryptoValues);
        Optional<CryptoValue> maxCryptoValue = cryptoStatsCalculator.getMax(cryptoValues);

        CryptoStatsDTO stats = new CryptoStatsDTO();

        if (oldestCryptoValue.isPresent()) {
            stats.setOldestValue(oldestCryptoValue.get().getPrice());
        }

        if (newestCryptoValue.isPresent()) {
            stats.setNewestValue(newestCryptoValue.get().getPrice());
        }

        if (minCryptoValue.isPresent()) {
            stats.setMinValue(minCryptoValue.get().getPrice());
        }

        if (maxCryptoValue.isPresent()) {
            stats.setMaxValue(maxCryptoValue.get().getPrice());
        }

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

    public Optional<NormalizedRangeDTO> getHighestNormalizedRange(String dateTimestamp) {
        List<String> cryptoNames = getCryptoNamesFromResourceFiles();
        return cryptoNames
                .stream().map(name -> {
                    List<CryptoValue> cryptoValues = cryptoValueDAO.getCryptoValuesByNameAndDateTimestamp(name,
                            dateTimestamp);
                    NormalizedRange normalizedRange = cryptoStatsCalculator.getNormalizedRange(name, cryptoValues);
                    return new NormalizedRangeDTO(normalizedRange.getName(), normalizedRange.getRange());
                })
                .max(Comparator.comparing(NormalizedRangeDTO::getRange));
    }

    public List<String> getCryptoNamesFromResourceFiles() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:prices/*.csv");
            return Stream.of(resources)
                         .map(resource -> resource.getFilename().replace("_values.csv", ""))
                         .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Could get normalized ranges due to following error", e);
        }
        return Collections.emptyList();
    }
}
