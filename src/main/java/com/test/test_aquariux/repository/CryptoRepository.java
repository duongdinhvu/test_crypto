package com.test.test_aquariux.repository;

import com.test.test_aquariux.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

    @Query(value = "SELECT id, symbol, bid_price, ask_price FROM crypto WHERE bid_price = (SELECT max(bid_price) FROM crypto)")
    Crypto maxBidPrice();
    @Query(value = "SELECT id, symbol, bid_price, ask_price  FROM crypto WHERE ask_price = (SELECT min(ask_price) FROM crypto)")
    Crypto minAskPrice();

}
