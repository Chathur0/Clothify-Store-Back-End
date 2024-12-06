package edu.fast_track.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.*;
import edu.fast_track.entity.CustomerEntity;
import edu.fast_track.entity.Order;
import edu.fast_track.entity.OrderDetails;
import edu.fast_track.entity.ProductEntity;
import edu.fast_track.repository.CustomerRepository;
import edu.fast_track.repository.OrderDetailRepository;
import edu.fast_track.repository.OrderRepository;
import edu.fast_track.repository.ProductRepository;
import edu.fast_track.repository.impl.OrderRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper mapper;
    private final OrderRepositoryImpl customOrderRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void saveOrder(OrderRequest orderRequest) {
        Order order = orderRepository.save(new Order(0, orderRequest.getTotalCost(), LocalDate.now(), 1, orderRequest.getCustomer().getId()));

        for (OrderRequest.CartItem cartItem : orderRequest.getCart()) {
            OrderDetails orderDetail = mapper.convertValue(cartItem, OrderDetails.class);
            orderDetail.setOrderId(order.getOrderId());
            ProductEntity product = mapper.convertValue(productRepository.findById(orderDetailsRepository.save(orderDetail).getProductId()), ProductEntity.class);
            product.setQty(product.getQty() - orderDetail.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return customOrderRepository.getAllOrders();
    }

    @Override
    public FullOrderDetails getFullOrderDetails(Integer id) {
        Order order = mapper.convertValue(orderRepository.findById(id), Order.class);
        CustomerEntity user = mapper.convertValue(customerRepository.findById(order.getUserId()), CustomerEntity.class);
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByOrderId(id);
        List<Integer> productIds = new ArrayList<>();
        for (OrderDetails aOrder : orderDetailsList) {
            productIds.add(aOrder.getProductId());
        }
        List<ProductEntity> allByIds = productRepository.findAllByIds(productIds);
        List<ProductDetails> productDetailsList = new ArrayList<>();

        for (int i = 0; i < allByIds.size() ; i++) {
            ProductEntity product = allByIds.get(i);
            OrderDetails orderDetails = orderDetailsList.get(i);
            productDetailsList.add(new ProductDetails(
                    product.getName(),
                    product.getCategory(),
                    "http://localhost:8080/"+product.getImage(),
                    orderDetails.getQuantity(),
                    orderDetails.getPrice()
            ));
        }

        return new FullOrderDetails(
                order.getOrderId(),
                order.getCost(),
                order.getDate(),
                order.getStatus(),
                mapper.convertValue(user,CustomerDetails.class),
                productDetailsList
        );
    }

    @Override
    public void updateOrderDetails(Integer id, Integer status) {
        Order order = mapper.convertValue(orderRepository.findById(id), Order.class);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> hasOrderedBefore(Integer id) {
        return orderRepository.findByUserId(id);
    }
}
