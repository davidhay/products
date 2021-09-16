package com.davidhay.jlassignment.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.domain.inbound.Product;
import java.util.List;

import com.davidhay.jlassignment.domain.inbound.ProductType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

@SpringBootTest
@ActiveProfiles("production")
@TestPropertySource(properties = "app.catalogURL=https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses&key=${API_KEY}")
@Disabled
/**
 * This integration test requires API_KEY to be set as an environmental variable.
 * It will get data from actual 'JL' product inbound.
 */
public class ProductCatalogRepositoryIT {

  @Autowired
  ProductCatalogRepository repo;

  @Autowired
  Environment environment;

  @Test
  void testCanReadFromActualCatalog(){

    assertArrayEquals(new String[]{"production"}, environment.getActiveProfiles());

    String apiKey = environment.getProperty("API_KEY");
    Assert.isTrue(apiKey != null, "The API_KEY must be set via an environmental variable, it's a secret!");

    List<Product> products = repo.getProductsFromCatalog(ProductType.DRESSES);
    assertTrue(products.size() > 0);

  }

}
