package com.test.test_aquariux.controller;



import com.google.gson.Gson;
import com.test.test_aquariux.dto.CryptoDTO;
import com.test.test_aquariux.entity.*;
import com.test.test_aquariux.exception.CustomException;
import com.test.test_aquariux.service.CryptoService;
import com.test.test_aquariux.service.TradingTransactionService;
import com.test.test_aquariux.service.UserService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    @Autowired
    UserService userService;

    @Autowired
    TradingTransactionService tradingTransactionService;

    @GetMapping("/best-price")
    public ResponseEntity<List<Crypto>> retrieveBestLatestPrice() {
        Crypto cryptoBidPrice = null;
        Crypto cryptoAskPrice = null;
        List<Crypto> cryptoList = new ArrayList<>();
        try {
            cryptoBidPrice = cryptoService.getBestLatestBidPrice();
            cryptoAskPrice = cryptoService.getBestLatestAskPrice();
            cryptoList.add(cryptoBidPrice);
            cryptoList.add(cryptoAskPrice);
        } catch (Exception ex) {
            throw new CustomException("Error retrieve Best latest price", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(cryptoList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<BigDecimal> retrieveWalletBalance(@PathVariable Long id) {
        BigDecimal walletBalance;
        try {
            walletBalance = userService.getWalletBalance(id);
        } catch (Exception ex) {
            throw new CustomException("Error retrieve Wallet balance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(walletBalance);
    }

    @GetMapping("/trading-history/{id}")
    public ResponseEntity<List<TradingTransaction>> getTradingHistory(@PathVariable Long id) {
        List<TradingTransaction> tradingTransactions = new ArrayList<TradingTransaction>();
        try {
            tradingTransactions = tradingTransactionService.getTradingHistory(id);
        } catch (Exception ex) {
            throw new CustomException("Error retrieve Trading history", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(tradingTransactions);
    }

    @PostMapping("/trading")
    public ResponseEntity<TradingTransaction> tradeCrypto(@RequestParam("crypto") String typeCrypto,
                                                          @RequestParam("type") String typeTrade,
                                                          @RequestParam("quantity") BigDecimal quantity,
                                                          @RequestParam("crypto_id") Long id,
                                                          @RequestParam("user_id") Long userId) {

        TradingTransaction tradingTransaction = new TradingTransaction();
        try {
            Optional<Crypto> crypto = cryptoService.getCryptoById(id);
            if (crypto != null) {
                tradingTransaction.setTransactionType(TransactionType.valueOf(typeTrade));
                tradingTransaction.setQuantity(quantity);
                tradingTransaction.setCrypto(crypto.get());

                BigDecimal unitPrice;
                if (typeTrade.equalsIgnoreCase(String.valueOf(TransactionType.SELL))) {
                    unitPrice = crypto.get().getBidPrice();
                } else {
                    unitPrice = crypto.get().getAskPrice();
                }
                BigDecimal total = quantity.add(unitPrice);

                tradingTransaction.setPrice(total);

                // refresh Wallet balance
                Optional<User> user = userService.getUserById(userId);
                user.get().setWalletBalance(user.get().getWalletBalance().add(total));
                userService.save(user.get());
            }

        } catch (Exception ex) {
            throw new CustomException("Error trading process", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(tradingTransaction);
    }
}
