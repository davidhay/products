package com.davidhay.jlassignment.repository;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductCatalogResponse;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class ProductCatalogRepository {

  private final RestTemplate restTemplate;
  private final URL catalogURL;

  @Autowired
  public ProductCatalogRepository(RestTemplate restTemplate,
      URL catalogURL) {
    this.restTemplate = restTemplate;
    this.catalogURL = catalogURL;
  }

  public List<Product> getProductsFromCatalog() {
    try {
      HttpHeaders headers = new HttpHeaders();
      // set `accept` header
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      // we must provide 'User-Agent'!
      headers.set(HttpHeaders.USER_AGENT, "SpringBootApp");

      // build the request
      HttpEntity<Void> request = new HttpEntity<>(headers);

      // use `exchange` method for HTTP call
      ResponseEntity<ProductCatalogResponse> response = restTemplate.exchange(this.catalogURL.toString(), HttpMethod.GET, request, ProductCatalogResponse.class);

      if (!response.getStatusCode().is2xxSuccessful()) {
        return Collections.emptyList();
      }
      ProductCatalogResponse body = response.getBody();
      if (body == null) {
        return Collections.emptyList();
      }
      List<Product> products = body.getProducts();
      if (products == null) {
        return Collections.emptyList();
      }
      return products;

    } catch (RuntimeException ex) {
      log.error("unexpected inbound error", ex);
      return Collections.emptyList();
    }
  }

}
