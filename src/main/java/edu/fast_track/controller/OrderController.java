package edu.fast_track.controller;

import edu.fast_track.dto.OrderRequest;
import edu.fast_track.entity.Order;
import edu.fast_track.service.JwtService;
import edu.fast_track.service.OrderService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;

    @PostMapping("/buy-products")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest orderRequest,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtService.validateToken(token);
            orderService.saveOrder(orderRequest);
            return ResponseEntity.ok("Order saved successfully");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/get-orders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtService.validateToken(token);
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<?> getFullOrderDetails(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer id) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtService.validateToken(token);
            return ResponseEntity.ok(orderService.getFullOrderDetails(id));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PutMapping("/update-order-status/{id}")
    public ResponseEntity<?> updateOrderStatus(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer id,@RequestBody Integer status) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtService.validateToken(token);
            orderService.updateOrderDetails(id,status);
            return ResponseEntity.ok("Order status updated..!");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/order-history/{id}")
    public ResponseEntity<?> hasOrderedBefore(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer id){
        try {
            jwtService.validateToken(authorizationHeader);
            List<Order> orders = orderService.hasOrderedBefore(id);
            return ResponseEntity.ok(orders);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

}
