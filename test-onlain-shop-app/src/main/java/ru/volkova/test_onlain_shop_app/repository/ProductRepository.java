package ru.volkova.test_onlain_shop_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.volkova.test_onlain_shop_app.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameStartsWithIgnoreCase(@Param("productName") String productName);
}
