package com.shoestore.controllers;

import com.shoestore.components.LocalizationUtils;
import com.shoestore.dtos.OrderDetailDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.OrderDetail;
import com.shoestore.repositories.OrderDetailRepository;
import com.shoestore.response.OrderDetailResponse;
import com.shoestore.services.Implement.OrderDetailServiceImpl;
import com.shoestore.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailServiceImpl orderDetailService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid  @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{orderDetailId}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable Long orderDetailId) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(orderDetailId);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Return list order detail of order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(
                OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }
    @PutMapping("/{orderDetailId}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long orderDetailId,
            @RequestBody OrderDetailDTO orderDetailDTO) {
        // Find order detail
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(orderDetailId, orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body(localizationUtils.getLocalizedMessaged(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY, id));
    }
}
