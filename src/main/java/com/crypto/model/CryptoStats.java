package com.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoStats {
    private Double oldestValue;
    private Double newestValue;
    private Double minValue;
    private Double maxValue;
}
