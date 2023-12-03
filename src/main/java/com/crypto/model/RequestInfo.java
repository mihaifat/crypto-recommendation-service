package com.crypto.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RequestInfo {
    private String ip;
    private String requestPath;
    private LocalDateTime timestamp;
}
