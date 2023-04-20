package com.test.test_aquariux.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trading_transactions")
@Data
public class TradingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id", referencedColumnName = "id")
    private Crypto crypto;

    @Column(name = "transaction_type")
    private TransactionType  transactionType;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
