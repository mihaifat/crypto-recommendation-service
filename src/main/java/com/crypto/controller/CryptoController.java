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

    @GetMapping("/{name}/stats")
    public ResponseEntity<CryptoStatsDTO> getCryptoStats(@PathVariable String name) {
        CryptoStatsDTO stats = cryptoService.getCryptoStats(name);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/normalized-ranges")
    public ResponseEntity<List<NormalizedRangeDTO>> getNormalizedRanges() {
        List<NormalizedRangeDTO> normalizedRanges = cryptoService.getNormalizedRanges();
        return ResponseEntity.ok(normalizedRanges);
    }

    @GetMapping("/{dateTimestamp}/highest-normalized-range")
    public ResponseEntity<NormalizedRangeDTO> getHighestNormalizedRange(@PathVariable String dateTimestamp) {
        Optional<NormalizedRangeDTO> normalizedRanges = cryptoService.getHighestNormalizedRange(dateTimestamp);
        return normalizedRanges.isPresent() ? ResponseEntity.ok(normalizedRanges.get()) : ResponseEntity.notFound().build();
    }
}
