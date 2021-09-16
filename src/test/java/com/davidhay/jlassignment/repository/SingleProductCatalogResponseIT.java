package com.davidhay.jlassignment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.davidhay.jlassignment.domain.inbound.Product;
import java.util.List;

import com.davidhay.jlassignment.domain.inbound.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest
public class SingleProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  ProductCatalogRepository repo;

  @Test
  public void testSingleResponse() {
    List<Product> products = repo.getProductsFromCatalog(ProductType.DRESSES);
    assertThat(products).size().isEqualTo(1);
  }

  @Override
  public String[] getStubs() {
    return Stubs.SINGLE_PRODUCT_STUBS;
  }

}
