package edu.fast_track.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Product;
import edu.fast_track.entity.ProductEntity;
import edu.fast_track.exception.CustomerExceptionHandler;
import edu.fast_track.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ObjectMapper mapper;
    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Override
    public List<Product> getMensProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(1)) {
            products.add(mapper.convertValue(product, Product.class));
        }
        return products;
    }

    @Override
    public List<Product> getWomenProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(2)) {
            products.add(mapper.convertValue(product, Product.class));
        }
        return products;
    }

    @Override
    public List<Product> getKidsProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(3)) {
            products.add(mapper.convertValue(product, Product.class));
        }
        return products;
    }

    @Override
    public List<Product> getBabyProducts() {
        List<Product> products = new ArrayList<>();
        for (ProductEntity product : productRepository.findByCategory(4)) {
            products.add(mapper.convertValue(product, Product.class));
        }
        return products;
    }

    @Override
    public Product getProductById(Integer id) {
        return mapper.convertValue(productRepository.findById(id), Product.class);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public void addProduct(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            if (product.getImage() != null && !product.getImage().isEmpty() && !imageService.deleteProduct(product.getImage())) {
                throw new CustomerExceptionHandler("Failed to delete previous image");
            }
            product.setImage(imageService.uploadProduct(image));
        } else {
            product.setImage(product.getImage().replace("http://localhost:8080/", ""));
        }
        productRepository.save(mapper.convertValue(product, ProductEntity.class));
    }
}
