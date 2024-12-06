package edu.fast_track.repository.impl;

import edu.fast_track.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;

    public List<OrderResponse> getAllOrders() {
        return jdbcTemplate.query("SELECT o.order_id, o.date, o.status, u.number FROM `order` o INNER JOIN user u ON o.user_id = u.id",
                (rs, rowNum) -> new OrderResponse(
                        rs.getInt("order_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("status"),
                        rs.getString("number")
                )
        );
    }
}
