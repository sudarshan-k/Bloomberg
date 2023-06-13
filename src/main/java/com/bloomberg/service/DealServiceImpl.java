package com.bloomberg.service;

import com.bloomberg.entity.Deal;
import com.bloomberg.exceptions.INValidISOException;
import com.bloomberg.model.DealResponse;
import com.bloomberg.repository.DealRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Set;

@Service
@Slf4j
public class DealServiceImpl implements DealService {

    private DealRepository dealRepository;

    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public DealResponse persistDeal(Deal deal) throws INValidISOException {

        Deal deal1;

        if (validateISO(deal.getSourceCurrencyISO()) && validateISO(deal.getTargetCurrencyISO())) {
            log.info("Persist the value in database : " + deal);
            deal1 = dealRepository.save(deal);
            return DealResponse.builder().status("success").deal(deal1).build();
        } else {
            throw new INValidISOException
                    ("Invalid currency ISO from the given inputs source currency iso: " + deal.getSourceCurrencyISO()
                            + " or target currency ISO:" + deal.getTargetCurrencyISO());
        }
    }

    private boolean validateISO(String currencyISO) {
        log.info("Inside validateISO method with code : " + currencyISO);
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        for (Currency currency : currencies) {
            if (currency.getCurrencyCode().equalsIgnoreCase(currencyISO)) {
                return true;
            }
        }
        return false;
    }
}
