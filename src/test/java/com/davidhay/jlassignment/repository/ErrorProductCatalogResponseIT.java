package com.davidhay.jlassignment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.davidhay.jlassignment.domain.inbound.Product;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest
public class ErrorProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  ProductCatalogRepository repo;

  @Test
  public void testErrorResponse() {
    List<Product> products = repo.getProductsFromCatalog("dresses");
    assertThat(products).isEmpty();
  }

  @Override
  public String[] getStubs() {
    return Stubs.ERROR_STUBS;
  }
}
