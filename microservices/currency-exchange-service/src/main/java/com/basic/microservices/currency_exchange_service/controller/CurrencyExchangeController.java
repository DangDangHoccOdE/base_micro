package com.basic.microservices.currency_exchange_service.controller;

import com.basic.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.basic.microservices.currency_exchange_service.repo.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
    @Autowired
    private Environment env;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to){
//        CurrencyExchange currencyExchange = new CurrencyExchange(100L,from,to, BigDecimal.valueOf(50),"8080");

       CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
       if(currencyExchange == null){
           throw new RuntimeException("Unable to find data for " + from + " to " + to);
       }
        String port = env.getProperty("local.server.port");
        currencyExchange.setEnviroment(port);

        return currencyExchange;
    }
}
