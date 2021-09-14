package com.davidhay.jlassignment.domain.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class SearchProductCatalogResponseTest {

  @Autowired
  ObjectMapper mapper;

  @Value("classpath:catalog/exampleProductCatalogResponse.json")
  Resource exampleProductCatalogResponse;

  ProductCatalogResponse getProductCatalogResponse(Resource res) throws Exception {
    return mapper.reader().readValue(res.getInputStream(), ProductCatalogResponse.class);
  }

  @Test
  void testExampleProductCatalogResponse() throws Exception {
    ProductCatalogResponse resp = getProductCatalogResponse(this.exampleProductCatalogResponse);
    assertNotNull(resp);
    List<Product> products = resp.getProducts();
    assertThat(products.size()).isEqualTo(24);
  }

}
