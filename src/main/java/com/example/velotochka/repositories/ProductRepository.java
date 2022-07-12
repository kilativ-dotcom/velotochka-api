package com.example.velotochka.repositories;

import com.example.velotochka.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByCategoryName(String category);
    List<Product> findByFeatures_NameAndFeatures_Value(String name, String value);
}
