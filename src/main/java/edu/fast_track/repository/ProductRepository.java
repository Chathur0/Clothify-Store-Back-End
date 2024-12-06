package edu.fast_track.repository;

import edu.fast_track.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {
    List<ProductEntity> findByCategory(int category);
    @Query("SELECT p FROM ProductEntity p WHERE p.id IN :productIds")
    List<ProductEntity> findAllByIds(@Param("productIds") List<Integer> productIds);
}