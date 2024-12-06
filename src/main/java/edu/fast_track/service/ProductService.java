package edu.fast_track.service;

import edu.fast_track.dto.Product;
import edu.fast_track.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);

    List<Product> getMensProducts();

    List<Product> getWomenProducts();

    List<Product> getKidsProducts();

    List<Product> getBabyProducts();

    Product getProductById(Integer id);

    void deleteProduct(Integer id);
}
