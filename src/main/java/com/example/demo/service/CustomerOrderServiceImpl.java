package com.example.demo.service;

import com.example.demo.dto.CustomerOrderInfo;
import com.example.demo.service.CustomerOrderService;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Override
    public String processCustomerOrder(CustomerOrderInfo info) {
        // You can add logic to store this info in DB, call another service, etc.
        return "Order '" + info.getOrderName() + "' received for customer ID: " + info.getCustomerId();
    }
}
