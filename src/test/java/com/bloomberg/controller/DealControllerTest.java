package com.bloomberg.controller;

import com.bloomberg.entity.Deal;
import com.bloomberg.exceptions.INValidISOException;
import com.bloomberg.model.DealResponse;
import com.bloomberg.service.DealService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(DealController.class)
public class DealControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DealService dealService;

    DealResponse response;
    Deal deal;

    ObjectMapper objectMapper = new ObjectMapper();

    String jsonRequest;

    @BeforeEach
    void setUp() throws JsonProcessingException {
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

        jsonRequest = objectMapper.writeValueAsString(deal);
    }

    @Test
    @DisplayName("Persist Deal - Valid")
    void persistDealValid() throws Exception {
        Mockito.when(dealService.persistDeal(deal)).thenReturn(response);

        String jsonRequest = objectMapper.writeValueAsString(deal);

        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deal.sourceCurrencyISO").value("PTE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deal.targetCurrencyISO").value("RUR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deal.amount").value(500));

    }

    @Test
    @DisplayName("Persist Deal - INValidISOException")
    void persistDealINValidISOException() throws Exception {

        Mockito.when(dealService.persistDeal(deal)).thenThrow(INValidISOException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("error"));

    }

    @Test
    @DisplayName("Persist Deal - Internal Server Exception")
    void persistDealInternalServerException() throws Exception {

        Mockito.doThrow(NullPointerException.class).when(dealService).persistDeal(deal);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("Persist Deal - Bad Request Case I")
    void persistDealBadRequestCASEI() throws Exception {

        deal.setSourceCurrencyISO(" ");
        String jsonRequest1 = objectMapper.writeValueAsString(deal);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .content(jsonRequest1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Persist Deal - Bad Request Case II")
    void persistDealBadRequestCASEII() throws Exception {

        deal.setTargetCurrencyISO(" ");
        String jsonRequest1 = objectMapper.writeValueAsString(deal);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .content(jsonRequest1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
