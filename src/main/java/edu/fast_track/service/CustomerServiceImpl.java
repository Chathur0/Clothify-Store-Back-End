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

import java.io.IOException;
import java.util.Base64;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final ObjectMapper mapper;
    private final JwtService jwtService;
    private final CustomerRepositoryImpl customRepository;
    private final ImageService imageService;

    @Override
    public String validateUser(Customer customer) {
        CustomerEntity customerEntity = repository.findByEmailAndDelete(customer.getEmail(), 0);
        if (customerEntity == null) {
            throw new CustomerExceptionHandler("User not found");
        }
        if (customerEntity.getPassword() != null){
            if (!BCrypt.checkpw(customer.getPassword(), customerEntity.getPassword())) {
                throw new CustomerExceptionHandler("Invalid credentials");
            }
        }else{
            throw new CustomerExceptionHandler("Password not set use login via social platform");
        }
        return generateJwtToken(customerEntity);
    }

    @Override
    public String getRole(String token) {
        CustomerEntity customer = repository.findByEmailAndDelete(jwtService.extractEmail(token), 0);
        if (customer == null) {
            throw new CustomerExceptionHandler("User not found");
        }
        return customRepository.isAdmin(customer.getId()) ? "admin" : "user";
    }

    @Override
    public CustomerDetails getUser(String token) {
        CustomerDetails customer = mapper.convertValue(repository.findByEmailAndDelete(jwtService.extractEmail(token), 0), CustomerDetails.class);
        String image = customer.getImage();
        if (image != null) {
            customer.setImage(image);
        } else {
            customer.setImage("");
        }
        return customer;
    }

    @Override
    public String getProfileImage(String token) {
        String image = repository.findByEmailAndDelete(jwtService.extractEmail(token), 0).getImage();
        return image != null ? image : "";
    }

    @Override
    public void deleteCustomer(int id) {
        CustomerEntity customerEntity = mapper.convertValue(repository.findById(id), CustomerEntity.class);
        customerEntity.setDelete(1);
        repository.save(customerEntity);
    }

    @Override
    public void updateCustomer(CustomerDetails customer, MultipartFile image) throws Exception {
        CustomerEntity savedCustomer = mapper.convertValue(repository.findById(customer.getId()), CustomerEntity.class);
        CustomerEntity customerWithNewData = mapper.convertValue(customer, CustomerEntity.class);

        if (!savedCustomer.getEmail().equalsIgnoreCase(customer.getEmail()) && repository.findByEmailAndDelete(customer.getEmail(), 0) != null) {
            throw new CustomerExceptionHandler("User with this email already exists");
        }
        customerWithNewData.setPassword(savedCustomer.getPassword());
        if (image != null && !image.isEmpty()) {
            if (customerWithNewData.getImage() != null && !customerWithNewData.getImage().isEmpty() && !imageService.deleteProfile(customer.getImage())) {
                throw new CustomerExceptionHandler("Failed to delete previous image");
            }
            customerWithNewData.setImage(imageService.uploadProfile(image));
        } else {
            customerWithNewData.setImage(savedCustomer.getImage());
        }
        repository.save(customerWithNewData);
    }

    @Override
    public void addUser(String userJson, MultipartFile profileImage) throws IOException {
        Customer user = mapper.readValue(userJson, Customer.class);
        if (repository.findByEmailAndDelete(user.getEmail(), 0) != null) {
            throw new CustomerExceptionHandler("User with this email already exists");
        }
        if (profileImage != null && !profileImage.isEmpty()) {
            user.setImage(imageService.uploadProfile(profileImage));
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        repository.save(mapper.convertValue(user, CustomerEntity.class));
    }

    @Override
    public String loginViaSocialMedia(Customer customer) {
        if (customer.getEmail() == null) {
            throw new CustomerExceptionHandler("Login failed");
        }
        CustomerEntity byEmailAndDelete = repository.findByEmailAndDelete(customer.getEmail(), 0);
        if (byEmailAndDelete != null) {
            return generateJwtToken(byEmailAndDelete);
        }
        return generateJwtToken(repository.save(mapper.convertValue(customer, CustomerEntity.class)));
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
