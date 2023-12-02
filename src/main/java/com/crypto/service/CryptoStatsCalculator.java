package com.crypto.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crypto.model.CryptoValue;
import com.crypto.model.NormalizedRange;

@Service
public class CryptoStatsCalculator {

    public Optional<CryptoValue> getOldest(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().min(Comparator.comparing(CryptoValue::getTimestamp));
    }

    public Optional<CryptoValue> getNewest(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().max(Comparator.comparing(CryptoValue::getTimestamp));
    }

    public Optional<CryptoValue> getMin(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().min(Comparator.comparing(CryptoValue::getPrice));
    }

    public Optional<CryptoValue> getMax(List<CryptoValue> cryptoValues) {
        return cryptoValues.stream().max(Comparator.comparing(CryptoValue::getPrice));
    }

    public NormalizedRange getNormalizedRange(String name, List<CryptoValue> cryptoValues) {
        Optional<CryptoValue> maxValue = getMax(cryptoValues);
        Optional<CryptoValue> minValue = getMin(cryptoValues);
        if (minValue.isPresent() && maxValue.isPresent()) {
            double range = (maxValue.get().getPrice() - minValue.get().getPrice()) / minValue.get().getPrice();
            return new NormalizedRange(name, range);
        }
        return new NormalizedRange(name, -100); // TODO: rethink handling of missing min and max values
    }

}
