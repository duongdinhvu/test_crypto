package com.test.test_aquariux.repository;

import com.test.test_aquariux.entity.TradingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingRepository extends JpaRepository<TradingTransaction, Long> {

    List<TradingTransaction> findTradingTransactionByUserId(Long id);
}
