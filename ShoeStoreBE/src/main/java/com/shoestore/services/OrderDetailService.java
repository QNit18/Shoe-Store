package com.shoestore.services;

import com.shoestore.dtos.OrderDetailDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;

    void deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId);
}
