package edu.fast_track.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.fast_track.dto.Customer;
import edu.fast_track.dto.CustomerDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomerService {

    String validateUser(Customer customer);

    String getRole(String token);

    CustomerDetails getUser(String bearer);

    String getProfileImage(String token);

    void deleteCustomer(int id);

    void updateCustomer(CustomerDetails customer, MultipartFile image) throws Exception;

    void addUser(String userJson, MultipartFile profileImage) throws IOException;

    String loginViaSocialMedia(Customer customer);
}
