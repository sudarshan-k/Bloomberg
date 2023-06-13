package com.bloomberg.service;

import com.bloomberg.entity.Deal;
import com.bloomberg.exceptions.INValidISOException;
import com.bloomberg.model.DealResponse;
import com.bloomberg.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DealServiceImplTest {

    @Autowired
    private DealService dealService;

    @MockBean
    private DealRepository dealRepository;

    DealResponse response;
    Deal deal;

    @BeforeEach
    void setUp() {
        response = DealResponse.builder().status("success").deal(Deal.builder()
                .id(1)
                .sourceCurrencyISO("PTE")
                .targetCurrencyISO("RUR")
                .amount(500)
                .build()).build();

        deal = Deal.builder().sourceCurrencyISO("PTE")
                .targetCurrencyISO("RUR")
                .amount(500)
                .build();
    }

    @Test
    @DisplayName("Persist Deal - Valid")
    void persistDealValid() throws INValidISOException {

        Deal deal1 = Deal.builder().id(1)
                .sourceCurrencyISO("PTE")
                .targetCurrencyISO("RUR")
                .amount(500)
                .build();
        Mockito.when(dealRepository.save(deal)).thenReturn(deal1);
        var response = dealService.persistDeal(deal);
        assertEquals("success", response.getStatus());
        assertEquals("PTE", response.getDeal().getSourceCurrencyISO());
    }

    @Test
    @DisplayName("Persist Deal - Exception")
    void persistDealException() throws INValidISOException {

        Deal deal1 = Deal.builder().id(1)
                .sourceCurrencyISO("PTER")
                .targetCurrencyISO("RUR1")
                .amount(500)
                .build();
        assertThrows(INValidISOException.class,
                () -> dealService.persistDeal(deal1));


    }
}
