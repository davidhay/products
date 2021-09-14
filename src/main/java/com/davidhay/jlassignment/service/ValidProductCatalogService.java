package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.repository.ProductCatalogRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidProductCatalogService {

  private final ProductCatalogRepository repo;

  @Autowired
  ValidProductCatalogService(ProductCatalogRepository repo) {
    this.repo = repo;
  }

  public List<Product> getValidProducts() {
    return repo.getProductsFromCatalog().stream().filter(Product::hasValidNowPrice).collect(
        Collectors.toList());
  }
}
