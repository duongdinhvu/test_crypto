package com.test.test_aquariux.scheduler;

import com.google.gson.Gson;
import com.test.test_aquariux.dto.CryptoDTO;
import com.test.test_aquariux.entity.Crypto;
import com.test.test_aquariux.entity.TradingPair;
import com.test.test_aquariux.exception.CustomException;
import com.test.test_aquariux.service.CryptoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;


@Component
public class CryptoScheduler {

    private final String URL_BINANCE = "https://api.binance.com/api/v3/ticker/bookTicker";
    private final String URL_HOUBI = "https://api.huobi.pro/market/tickers";

    RestTemplate restTemplate;

    @Autowired
    CryptoService cryptoService;

    private final ModelMapper modelMapper;

    @Autowired
    Gson gson;

    public CryptoScheduler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


//    public void retrieve() throws MalformedURLException {
//        RestTemplate restTemplate = new RestTemplate();
//
//        CryptoDTO[] response = restTemplate.getForObject(URL_BINANCE, CryptoDTO[].class);
//        retrieveLatestPrice(response, "URL_BINANCE");
//
////        restTemplate = new RestTemplate();
////        CryptoDTO[] response2 = new CryptoDTO[]{gson.fromJson(getJson(), CryptoDTO.class)};
////        retrieveLatestPrice(response2, "URL_HOUDI");
//    }

    @Scheduled(fixedRate = 10000)
    public ResponseEntity<CryptoDTO> retrieveLatestPrice() {

        RestTemplate restTemplate1 = new RestTemplate();

        CryptoDTO[] response = restTemplate1.getForObject(URL_BINANCE, CryptoDTO[].class);

        List<CryptoDTO> cryptos =
                Arrays.asList(response).stream()
                        .filter(u -> (u.getSymbol().equalsIgnoreCase(String.valueOf(TradingPair.ETHUSDT))))
                        .collect(Collectors.toList());

        CryptoDTO bestPriceCrypto = null;
        BigDecimal bidPrice;
        BigDecimal askPrice;

        try {
            bestPriceCrypto = cryptos
                    .stream()
                    .max(Comparator.comparing(CryptoDTO::getBidPrice))
                    .orElseThrow(NoSuchMethodException::new);
            bidPrice = bestPriceCrypto.getBidPrice();

            bestPriceCrypto = cryptos
                    .stream()
                    .max(Comparator.comparing(CryptoDTO::getAskPrice))
                    .orElseThrow(NoSuchMethodException::new);
            askPrice = bestPriceCrypto.getAskPrice();

            bestPriceCrypto.setBidPrice(bidPrice);
            bestPriceCrypto.setAskPrice(askPrice);


            cryptoService.saveCrypto(modelMapper.map(bestPriceCrypto, Crypto.class));
        } catch (NoSuchMethodException ex) {
            throw new CustomException("Error saved crypto  with Binance URL", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(bestPriceCrypto);

    }


   // @Scheduled(fixedRate = 10000)
//    public ResponseEntity<CryptoDTO> retrieveLatestPriceInHoudi() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        CryptoDTO[] response = restTemplate.getForObject(URL_HOUBI, CryptoDTO[].class);
//
//
//        List<CryptoDTO> cryptos =
//                Arrays.asList(response).stream()
//                        .filter(u -> (u.getSymbol().equalsIgnoreCase(String.valueOf(TradingPair.ETHUSDT))))
//                        .collect(Collectors.toList());
//
//        CryptoDTO bestPriceCrypto = null;
//        try {
//            bestPriceCrypto = cryptos
//                    .stream()
//                    .max(Comparator.comparing(CryptoDTO::getBidPrice))
//                    .orElseThrow(NoSuchMethodException::new);
//
//            cryptoService.saveCrypto(modelMapper.map(bestPriceCrypto, Crypto.class));
//        } catch (NoSuchMethodException ex) {
//            throw new CustomException("Error saved crypto  with Houdi URL", HttpStatus.NOT_FOUND);
//        }
//
//        return ResponseEntity.ok().body(bestPriceCrypto);
//
//    }

}
