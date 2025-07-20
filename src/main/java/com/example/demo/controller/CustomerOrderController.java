package com.example.demo.controller;

import com.example.demo.dto.CustomerOrderInfo;
import com.example.demo.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-orders")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @Autowired
    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitCustomerOrder(@RequestBody CustomerOrderInfo info) {
        String response = customerOrderService.processCustomerOrder(info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/demo")
    public CustomerOrderInfo demoCustomerOrder() {
        return new CustomerOrderInfo(123L, "Sample Order");
    }
}
