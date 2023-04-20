package com.test.test_aquariux.service;

import com.test.test_aquariux.entity.TradingTransaction;
import com.test.test_aquariux.repository.TradingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradingTransactionService {

    @Autowired
    TradingRepository tradingRepository;

    public List<TradingTransaction> getTradingHistory(Long id) {
        return tradingRepository.findTradingTransactionByUserId(id);
    }
}
