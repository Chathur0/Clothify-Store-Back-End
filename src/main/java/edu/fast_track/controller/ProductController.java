package edu.fast_track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Product;
import edu.fast_track.service.JwtService;
import edu.fast_track.service.ProductService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ObjectMapper mapper;
    private final JwtService jwtService;
    private final ProductService service;

    @PostMapping("/add-product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam("product") String productJson,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authHeader.substring(7);
        try {
            jwtService.validateToken(token);
            service.addProduct(mapper.readValue(productJson, Product.class),image);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
        return ResponseEntity.status(200).body("Product Added");
    }

    @GetMapping("/get-men-product")
    public List<Product> getMensProducts() {
        return service.getMensProducts();
    }

    @GetMapping("/get-women-product")
    public List<Product> getWomenProducts() {
        return service.getWomenProducts();
    }

    @GetMapping("/get-kids-product")
    public List<Product> getKidsProducts() {
        return service.getKidsProducts();
    }

    @GetMapping("/get-baby-product")
    public List<Product> getBabyProducts() {
        return service.getBabyProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Integer id) {
        return service.getProductById(id);
    }

    @PutMapping("/change-product")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String authHeader,
                                           @RequestParam("product") String productJson,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authHeader.substring(7);
        try {
            jwtService.validateToken(token);
            service.addProduct(mapper.readValue(productJson, Product.class),image);
        } catch (
                ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (
                JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (
                Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
        return ResponseEntity.status(200).body("Product Updated");
    }
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtService.validateToken(token);
            service.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}

