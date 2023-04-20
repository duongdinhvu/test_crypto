package com.test.test_aquariux.service;

import com.test.test_aquariux.dto.CryptoDTO;
import com.test.test_aquariux.entity.Crypto;
import com.test.test_aquariux.repository.CryptoRepository;
import com.test.test_aquariux.repository.TradingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CryptoService {

    private final String URL_BINANCE = "https://api.binance.com/api/v3/ticker/bookTicker";
    private final String URL_HOUBI = "https://api.huobi.pro/market/tickers";

    @Autowired
    private TradingRepository tradingRepository;
    @Autowired
    private CryptoRepository cryptoRepository;

    public void saveCrypto(Crypto crypto) {
        cryptoRepository.save(crypto);
    }

    public Crypto getBestLatestBidPrice() {
        return cryptoRepository.maxBidPrice();
    }

    public Crypto getBestLatestAskPrice() {
        return cryptoRepository.minAskPrice();
    }

    public Optional<Crypto> getCryptoById(Long id) {
        return cryptoRepository.findById(id);
    }
}
