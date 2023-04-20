package com.test.test_aquariux.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "crypto")
@Data
@NoArgsConstructor
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "bid_price")
    private BigDecimal bidPrice;

    @Column(name = "ask_price")
    private BigDecimal askPrice;
}
