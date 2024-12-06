package edu.fast_track.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Customer;
import edu.fast_track.dto.CustomerDetails;
import edu.fast_track.entity.CustomerEntity;
import edu.fast_track.exception.CustomerExceptionHandler;
import edu.fast_track.repository.CustomerRepository;
import edu.fast_track.repository.impl.CustomerRepositoryImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final ObjectMapper mapper;
    private final JwtService jwtService;
    private final CustomerRepositoryImpl customRepository;

    @Override
    public String validateUser(Customer customer) {
        CustomerEntity customerEntity = repository.findByEmailAndDelete(customer.getEmail(),0);
        if (customerEntity == null) {
            throw new CustomerExceptionHandler("User not found");
        }
        if (!BCrypt.checkpw(customer.getPassword(), customerEntity.getPassword())) {
            throw new CustomerExceptionHandler("Invalid credentials");
        }
        return generateJwtToken(customerEntity);
    }

    @Override
    public String getRole(String token) {
        CustomerEntity customer = repository.findByEmailAndDelete(jwtService.extractEmail(token),0);
        if (customer == null) {
            throw new CustomerExceptionHandler("User not found");
        }
        return customRepository.isAdmin(customer.getId()) ? "admin" : "user";
    }

    @Override
    public CustomerDetails getUser(String token) {
        CustomerDetails customer = mapper.convertValue(repository.findByEmailAndDelete(jwtService.extractEmail(token),0), CustomerDetails.class);
        String image = customer.getImage();
        customer.setImage(image != null ? "http://localhost:8080/" + image.replace("\\", "/") : "");
        return customer;
    }

    @Override
    public String getProfileImage(String token) {
        String image = repository.findByEmailAndDelete(jwtService.extractEmail(token),0).getImage();
        return image != null ? "http://localhost:8080/" + image.replace("\\", "/") : "";
    }

    @Override
    public void deleteCustomer(int id) {
        CustomerEntity customerEntity = mapper.convertValue(repository.findById(id), CustomerEntity.class);
        customerEntity.setDelete(1);
        repository.save(customerEntity);
    }

    @Override
    public void updateCustomer(CustomerDetails customer, MultipartFile image) throws IOException {
        CustomerEntity savedCustomer = mapper.convertValue(repository.findById(customer.getId()), CustomerEntity.class);
        CustomerEntity customerWithNewData = mapper.convertValue(customer, CustomerEntity.class);

        if (!savedCustomer.getEmail().equalsIgnoreCase(customer.getEmail()) && repository.findByEmailAndDelete(customer.getEmail(),0) != null) {
            throw new CustomerExceptionHandler("User with this email already exists");
        }
        customerWithNewData.setPassword(savedCustomer.getPassword());
        customerWithNewData.setDelete(savedCustomer.getDelete());
        if (image != null && !image.isEmpty()) {
            Path imagePath = Paths.get("uploads/profile-images", System.currentTimeMillis() + "_" + image.getOriginalFilename());
            image.transferTo(imagePath);
            if (customerWithNewData.getImage() != null && !customerWithNewData.getImage().isEmpty()) {
                File existingImage = new File(savedCustomer.getImage().replace("http://localhost:8080/", ""));
                if (existingImage.exists()) {
                    existingImage.delete();
                }
            }
            customerWithNewData.setImage(imagePath.toString());
        } else {
            customerWithNewData.setImage(customerWithNewData.getImage().replace("http://localhost:8080/", ""));
        }
        repository.save(customerWithNewData);
    }

    @Override
    public void addUser(String userJson, MultipartFile profileImage) throws IOException {
        Customer user = mapper.readValue(userJson, Customer.class);
        if (repository.findByEmailAndDelete(user.getEmail(),0) != null){
            throw new CustomerExceptionHandler("User with this email already exists");
        }
        if (profileImage != null && !profileImage.isEmpty()) {
            Path imagePath = Paths.get("uploads/profile-images", System.currentTimeMillis() + "_" + profileImage.getOriginalFilename());
            profileImage.transferTo(imagePath);
            user.setImage(imagePath.toString());
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        repository.save(mapper.convertValue(user, CustomerEntity.class));
    }

    private String generateJwtToken(CustomerEntity customerEntity) {
        return Jwts.builder()
                .setSubject(customerEntity.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes()))
                .compact();
    }
}
