package com.davidhay.jlassignment.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.controller.DressesController;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import com.davidhay.jlassignment.repository.BaseProductCatalogRespositoryIT;
import com.davidhay.jlassignment.repository.Stubs;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmptyProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  public void testEmptyResponseFromCatalog() {
    ResponseEntity<ProductsInfo> response = restTemplate.getForEntity(DressesController.REDUCED_PRICES_DRESSES_PATH,
        ProductsInfo.class);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertThat(response.getHeaders().getContentType()).isNotNull();
    assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
    ProductsInfo info = response.getBody();
    assertThat(info).isNotNull();
    List<ProductInfo> products = info.getProducts();
    assertThat(products).isNotNull();
    assertThat(products).isEmpty();
  }

  @Override
  public String[] getStubs() {
    return Stubs.EMPTY_STUBS;
  }


}
