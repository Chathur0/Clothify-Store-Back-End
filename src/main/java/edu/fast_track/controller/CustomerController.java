package edu.fast_track.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Customer;
import edu.fast_track.dto.CustomerDetails;
import edu.fast_track.exception.CustomerExceptionHandler;
import edu.fast_track.service.CustomerService;
import edu.fast_track.service.JwtService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService userService;
    private final ObjectMapper mapper;
    private final JwtService jwtService;

    @PostMapping("/add-customer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUser(@RequestParam("customer") String userJson,
                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            userService.addUser(userJson,profileImage);
            return ResponseEntity.ok("User added successfully");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error parsing user JSON");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred when image upload");
        } catch (CustomerExceptionHandler e){
            return ResponseEntity.status(406).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {
        try{
            return ResponseEntity.ok(userService.validateUser(customer));
        }catch (RuntimeException e){
            return ResponseEntity.status(406).body(e.getMessage());
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        try {
            CustomerDetails customer = userService.getUser(authorizationHeader.replace("Bearer ", ""));
            if (customer != null) {
                return ResponseEntity.ok(customer);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/get-user-role")
    public ResponseEntity<?> getUserRole(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authHeader.substring(7);
        try {
            jwtService.validateToken(token);
            return ResponseEntity.ok().body(Map.of(
                    "role", userService.getRole(token),
                    "profileImage", userService.getProfileImage(token)
            ));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Invalid user email"));
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }

    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateCustomer(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam("customer") String customerJson,
                                            @RequestParam(value = "newImage", required = false) MultipartFile image) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authHeader.substring(7);
        try {
            jwtService.validateToken(token);
            userService.updateCustomer(mapper.readValue(customerJson, CustomerDetails.class),image);
            return ResponseEntity.status(200).body("Customer Updated");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (CustomerExceptionHandler e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }
        String token = authHeader.substring(7);
        try {
            jwtService.validateToken(token);
            userService.deleteCustomer(id);
            return ResponseEntity.status(200).body("Customer Updated");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token has expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    
    @PostMapping("login-via-social-media")
    public ResponseEntity<?> loginViaSocialMedia(@RequestBody Customer customer) {
        try{
            return ResponseEntity.ok(userService.loginViaSocialMedia(customer));
        }catch (RuntimeException e){
            return ResponseEntity.status(406).body(e.getMessage());
        }
    }

}

