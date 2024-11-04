package com.sparta.realtomatoapp.order.controller;

import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateRequestDto;
import com.sparta.realtomatoapp.order.dto.OrderCreateResponseDto;
import com.sparta.realtomatoapp.order.repository.OrderRepository;
import com.sparta.realtomatoapp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<DataResponseDto<OrderCreateResponseDto>> createOrder(@RequestBody OrderCreateRequestDto requestDto){
        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("주문성공", List.of(responseDto)));
    }
}
