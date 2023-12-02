package com.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoStatsDTO {
    private Double oldestValue;
    private Double newestValue;
    private Double minValue;
    private Double maxValue;
}
