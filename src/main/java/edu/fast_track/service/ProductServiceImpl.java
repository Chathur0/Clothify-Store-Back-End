package edu.fast_track.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Product;
import edu.fast_track.entity.ProductEntity;
import edu.fast_track.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ObjectMapper mapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void addProduct(Product product) {
        productRepository.save(new ProductEntity(product.getId(), product.getQty(), product.getName(), product.getDescription(), product.getPrice(), product.getImage().replace("\\", "/"), product.getCategory()));
    }

    @Override
    public List<Product> getMensProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(1)) {
            products.add(new Product(product.getId(), product.getQty(), product.getName(), product.getDescription(), product.getPrice(), "http://localhost:8080/" + product.getImage(), product.getCategory()));
        }
        return products;
    }

    @Override
    public List<Product> getWomenProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(2)) {
            products.add(new Product(product.getId(), product.getQty(), product.getName(), product.getDescription(), product.getPrice(), "http://localhost:8080/" + product.getImage(), product.getCategory()));
        }
        return products;
    }

    @Override
    public List<Product> getKidsProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(3)) {
            products.add(new Product(product.getId(), product.getQty(), product.getName(), product.getDescription(), product.getPrice(), "http://localhost:8080/" + product.getImage(), product.getCategory()));
        }
        return products;
    }

    @Override
    public List<Product> getBabyProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(4)) {
            products.add(new Product(product.getId(), product.getQty(), product.getName(), product.getDescription(), product.getPrice(), "http://localhost:8080/" + product.getImage(), product.getCategory()));
        }
        return products;
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = mapper.convertValue(productRepository.findById(id), Product.class);
        product.setImage("http://localhost:8080/" + product.getImage());
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
