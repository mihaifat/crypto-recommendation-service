package com.crypto.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crypto.model.RequestInfo;

@Service
public class RateLimitService {
    
    @Value("${max.requests.per.minute}")
    private Integer maxRequestsPerHour; 

    private final Map<RequestInfo, Integer> requestCountsForARequestInfo = new HashMap<>();

    public boolean isWithinRateLimit(String ip, String requestPath) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withSecond(0).withNano(0);
        RequestInfo requestInfo = new RequestInfo(ip, requestPath, localDateTime);

        Integer currentCountOfInteger = requestCountsForARequestInfo.get(requestInfo);
        if (currentCountOfInteger == null) {
            currentCountOfInteger = 1;
            requestCountsForARequestInfo.put(requestInfo, currentCountOfInteger);
            return true;
        }
        currentCountOfInteger++;
        requestCountsForARequestInfo.put(requestInfo, currentCountOfInteger);
        return currentCountOfInteger <= maxRequestsPerHour;
    }
}
