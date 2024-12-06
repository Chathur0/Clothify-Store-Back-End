package edu.fast_track.repository;

import edu.fast_track.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {
    @Query("SELECT od FROM OrderDetails od WHERE od.orderId = :orderId")
    List<OrderDetails> findAllByOrderId(@Param("orderId") Integer orderId);

}
