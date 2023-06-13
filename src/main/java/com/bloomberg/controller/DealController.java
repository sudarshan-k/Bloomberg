package com.bloomberg.controller;

import com.bloomberg.entity.Deal;
import com.bloomberg.exceptions.INValidISOException;
import com.bloomberg.model.DealResponse;
import com.bloomberg.model.Error;
import com.bloomberg.service.DealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/deal")
@Slf4j
public class DealController {

    private DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping
    public ResponseEntity<DealResponse> persistDeal(@RequestBody Deal deal) {

        log.info(" Start persist Deal Method : " + deal.toString());
        DealResponse dealResponse;

        if (StringUtils.isEmpty(deal.getSourceCurrencyISO()) || StringUtils.isBlank(deal.getSourceCurrencyISO())) {
            dealResponse = DealResponse.builder().status("error").errors(Collections.singletonList(Error.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value()).errorDesc("Source Currency ISO is required ").
                    build())).build();

            return new ResponseEntity<>(dealResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(deal.getTargetCurrencyISO()) || StringUtils.isBlank(deal.getTargetCurrencyISO())) {
            dealResponse = DealResponse.builder().status("error").errors(Collections.singletonList(Error.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value()).errorDesc("Target Currency ISO is required ").
                    build())).build();

            return new ResponseEntity<>(dealResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            dealResponse = dealService.persistDeal(deal);

        } catch (INValidISOException ivie) {
            log.error("One or Both Invalid ISO Code in the given input" + deal.getTargetCurrencyISO() + "" + deal.getSourceCurrencyISO());
            dealResponse = DealResponse.builder().status("error").errors(Collections.singletonList(Error.builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .errorDesc(ivie.getCause() != null ? ivie.getCause().getMessage() : ivie.getMessage()).build())).build();
            return new ResponseEntity<>(dealResponse, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Unexpected Exception in persist Deal service", e);
            dealResponse = DealResponse.builder().status("error").errors(Collections.singletonList(Error.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorDesc(e.getCause() != null ? e.getCause().getMessage() : e.getMessage()).build())).build();
            return new ResponseEntity<>(dealResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(dealResponse, HttpStatus.CREATED);
    }
}
