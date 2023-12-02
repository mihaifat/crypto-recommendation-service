package com.crypto.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crypto.model.CryptoValue;
import com.crypto.model.NormalizedRange;

@Service
public class CryptoStatsCalculator {

    public CryptoValue getOldest(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().min(Comparator.comparing(CryptoValue::getTimestamp)).get();
    }

    public CryptoValue getNewest(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().max(Comparator.comparing(CryptoValue::getTimestamp)).get();
    }

    public CryptoValue getMin(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().min(Comparator.comparing(CryptoValue::getPrice)).get();
    }

    public CryptoValue getMax(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().max(Comparator.comparing(CryptoValue::getPrice)).get();
    }

    public NormalizedRange getNormalizedRange(String name, List<CryptoValue> cryptoValues) {
        CryptoValue maxValue = getMax(cryptoValues);
        CryptoValue minValue = getMin(cryptoValues);
        double range = (maxValue.getPrice() - minValue.getPrice()) / minValue.getPrice();
        return new NormalizedRange(name, range);
    } 

}
