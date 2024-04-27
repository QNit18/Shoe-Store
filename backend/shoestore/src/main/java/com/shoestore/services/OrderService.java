package com.shoestore.services;

import com.shoestore.dtos.OrderDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    Order getOrder(Long id) throws DataNotFoundException;

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id) throws DataNotFoundException;

    List<Order> findOrderByUserId(Long userId);
}
