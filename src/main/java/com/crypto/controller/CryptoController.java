package com.crypto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.dto.CryptoStatsDTO;
import com.crypto.dto.NormalizedRangeDTO;
import com.crypto.service.CryptoService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    /**
     * Retrieves statistics for a specified cryptocurrency.
     *
     * @param name The name of the cryptocurrency for which statistics are requested.
     * @return A {@link ResponseEntity} with {@link CryptoStatsDTO} containing the stats of the specified crypto.
     *         Returns OK (200) status if found, or an error status otherwise.
     */
    @GetMapping("/{name}/stats")
    public ResponseEntity<CryptoStatsDTO> getCryptoStats(@PathVariable String name) {
        CryptoStatsDTO stats = cryptoService.getCryptoStats(name);
        return ResponseEntity.ok(stats);
    }

    /**
     * Retrieves a list of normalized ranges for all cryptocurrencies.
     *
     * @return A {@link ResponseEntity} containing a list of {@link NormalizedRangeDTO}, each representing 
     *         the normalized range of a cryptocurrency. Returns OK (200) status if successfully retrieved.
     */
    @GetMapping("/normalized-ranges")
    public ResponseEntity<List<NormalizedRangeDTO>> getNormalizedRanges() {
        List<NormalizedRangeDTO> normalizedRanges = cryptoService.getNormalizedRanges();
        return ResponseEntity.ok(normalizedRanges);
    }

    /**
     * Retrieves the highest normalized range for cryptocurrencies on a specific date.
     *
     * @param dateTimestamp The timestamp of the date for the highest normalized range request.
     * @return A {@link ResponseEntity} with {@link NormalizedRangeDTO} representing the highest 
     *         normalized range on the specified date. Returns OK (200) if found, or NOT FOUND (404) otherwise.
     */
    @GetMapping("/{dateTimestamp}/highest-normalized-range")
    public ResponseEntity<NormalizedRangeDTO> getHighestNormalizedRange(@PathVariable String dateTimestamp) {
        Optional<NormalizedRangeDTO> normalizedRanges = cryptoService.getHighestNormalizedRange(dateTimestamp);
        return normalizedRanges.isPresent() ? ResponseEntity.ok(normalizedRanges.get())
                : ResponseEntity.notFound().build();
    }
}
