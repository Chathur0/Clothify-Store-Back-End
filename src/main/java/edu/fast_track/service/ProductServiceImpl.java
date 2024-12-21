package edu.fast_track.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fast_track.dto.Product;
import edu.fast_track.entity.ProductEntity;
import edu.fast_track.exception.CustomerExceptionHandler;
import edu.fast_track.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Product> getProductsByCategory(int category, int page) {
        return productRepository.findByCategory(category, PageRequest.of(page, 30)).map(productEntity -> mapper.convertValue(productEntity, Product.class));
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
        }
        productRepository.save(mapper.convertValue(product, ProductEntity.class));
    }
}
