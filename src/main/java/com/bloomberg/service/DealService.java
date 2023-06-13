package com.bloomberg.service;

import com.bloomberg.entity.Deal;
import com.bloomberg.exceptions.INValidISOException;
import com.bloomberg.model.DealResponse;

public interface DealService {

    DealResponse persistDeal(Deal deal) throws INValidISOException;
}
