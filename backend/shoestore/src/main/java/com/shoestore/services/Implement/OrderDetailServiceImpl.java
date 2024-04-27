package com.shoestore.services.Implement;

import com.shoestore.dtos.OrderDTO;
import com.shoestore.dtos.OrderDetailDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.Order;
import com.shoestore.models.OrderDetail;
import com.shoestore.models.Product;
import com.shoestore.repositories.OrderDetailRepository;
import com.shoestore.repositories.OrderRepository;
import com.shoestore.services.OrderDetailService;
import com.shoestore.services.OrderService;
import com.shoestore.services.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final ProductService productService;

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // Find order of order detail
        Order order = orderService.getOrder(orderDetailDTO.getOrderId());
        // Find Product
        Product product = productService.getProductById(orderDetailDTO.getProductId());

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        // Save into database
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot order detail with id: " + id)
        );
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        // Find order detail, order, product
        OrderDetail existOrderDetail = getOrderDetail(id);
        Order existOrder = orderService.getOrder(existOrderDetail.getOrder().getId());
        Product existProduct = productService.getProductById(existOrderDetail.getProduct().getId());

        existOrderDetail.setPrice(orderDetailDTO.getPrice());
        existOrderDetail.setColor(orderDetailDTO.getColor());
        existOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());

        existOrderDetail.setOrder(existOrder);
        existOrderDetail.setProduct(existProduct);

        return orderDetailRepository.save(existOrderDetail);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
