package com.test.test_aquariux.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CryptoDTO {

    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bid;
    private BigDecimal price;
    private BigDecimal askPrice;
}
