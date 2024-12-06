package edu.fast_track.service;

import edu.fast_track.dto.FullOrderDetails;
import edu.fast_track.dto.OrderRequest;
import edu.fast_track.dto.OrderResponse;
import edu.fast_track.entity.Order;

import java.util.List;

public interface OrderService {
    void saveOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    FullOrderDetails getFullOrderDetails(Integer id);

    void updateOrderDetails(Integer id, Integer status);

    List<Order> hasOrderedBefore(Integer id);
}
