package com.shoestore.services.Implement;

import com.shoestore.dtos.CartItemDTO;
import com.shoestore.dtos.OrderDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.*;
import com.shoestore.repositories.OrderDetailRepository;
import com.shoestore.repositories.OrderRepository;
import com.shoestore.repositories.ProductRepository;
import com.shoestore.repositories.UserRepository;
import com.shoestore.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // Find user_id existing ?
        User user = userRepository.findById(orderDTO.getUserId()).
                orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        // Covert OrderDTO to Order
        // Using library model Mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // Update fields of order from orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        // Shipping date >= order date
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        // Create list object Order Detail from Cart Items
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()){
            // Create a object Order detail from cart item
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Take info of product
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            // Find existing product
            Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

            // Info order details
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);

            orderDetail.setPrice(product.getPrice());
            order.setTotalMoney(product.getPrice() * cartItemDTO.getQuantity());

            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order getOrder(Long id) throws DataNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot order with id: " + id));
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = getOrder(id);
        User user = userRepository.findById(order.getUser().getId()).get();

        // Create mapping from orderDTO to order
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order order = getOrder(id);
        order.setActive(false);
    }

    @Override
    public List<Order> findOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
