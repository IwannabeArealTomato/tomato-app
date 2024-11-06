package com.sparta.realtomatoapp.order.controller;

import com.sparta.realtomatoapp.common.dto.BaseResponseDto;
import com.sparta.realtomatoapp.common.dto.DataResponseDto;
import com.sparta.realtomatoapp.order.dto.*;
import com.sparta.realtomatoapp.order.service.OrderService;
import com.sparta.realtomatoapp.security.Authorized;
import com.sparta.realtomatoapp.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Authorized(UserRole.USER)
    @PostMapping
    public ResponseEntity<DataResponseDto<OrderCreateResponseDto>> createOrder(@RequestBody OrderCreateRequestDto requestDto){
        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("주문성공", List.of(responseDto)));
    }

    @Authorized(UserRole.STOREOWNER)
    @PutMapping("/{orderId}")
    public ResponseEntity<DataResponseDto<OrderUpdateResponseDto>> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequestDto requestDto){
        OrderUpdateResponseDto responsDto = orderService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok(new DataResponseDto<>("주문수정완료", List.of(responsDto)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DataResponseDto<OrderResponseDto>> getOrder(@PathVariable Long orderId){
        OrderResponseDto responseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(new DataResponseDto<>("주문 단건 조회", List.of(responseDto)));
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<OrderResponseDto>> getAllOrder(){
        List<OrderResponseDto> responseDto = orderService.getAllOrder();
        return ResponseEntity.ok(new DataResponseDto<>("주문 다건 조회", responseDto));
    }

    @Authorized(UserRole.STOREOWNER)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponseDto> deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(BaseResponseDto.baseResponseBuilder().message("주문 삭제 성공").build());
    }

}

