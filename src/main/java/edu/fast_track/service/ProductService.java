package edu.fast_track.service;

import edu.fast_track.dto.Product;
import edu.fast_track.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<Product> getMensProducts();

    List<Product> getWomenProducts();

    List<Product> getKidsProducts();

    List<Product> getBabyProducts();

    Product getProductById(Integer id);

    void deleteProduct(Integer id);

    void addProduct(Product product, MultipartFile image) throws IOException;
}
