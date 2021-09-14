package com.davidhay.jlassignment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductCatalogResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
public class ProductCatalogRepositoryTest {

  public static final String FAKE_URL = "https://someserver/path?qu=1";

  @Mock
  RestTemplate mRestTemplate;

  URL productCatalogURL;

  ProductCatalogRepository sut;

  @Mock
  List<Product> mProducts;
  @Captor
  ArgumentCaptor<String> argURL;
  @Captor
  ArgumentCaptor<HttpEntity<Void>> argRequest;
  @Captor
  ArgumentCaptor<HttpMethod> argMethod;
  @Captor
  ArgumentCaptor<Class<ProductCatalogResponse>> argClass;

  @BeforeEach
  void setup() throws MalformedURLException {
    productCatalogURL = UriComponentsBuilder.fromUriString(FAKE_URL).build().toUri().toURL();
    sut = new ProductCatalogRepository(mRestTemplate, productCatalogURL);
  }

  @Test
  public void testLookupProducts() {
    ProductCatalogResponse response = new ProductCatalogResponse();
    response.setProducts(mProducts);

    when(mRestTemplate.exchange(argURL.capture(), argMethod.capture(), argRequest.capture(),
        argClass.capture())).thenReturn(
        ResponseEntity.ok(response));

    List<Product> result = sut.getProductsFromCatalog();
    assertThat(result).isEqualTo(mProducts);

    assertThat(argURL.getValue()).isEqualTo(FAKE_URL);
    verify(mRestTemplate).exchange(argURL.getValue(), argMethod.getValue(), argRequest.getValue(),
        argClass.getValue());

    verifyNoMoreInteractions(mRestTemplate, mProducts);
  }

  void checkLookupProductsFailed(int statusCode) {
    ProductCatalogResponse response = new ProductCatalogResponse();
    response.setProducts(mProducts);

    OngoingStubbing<ResponseEntity<ProductCatalogResponse>> temp = when(
        mRestTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
            any(Class.class)));

    if (statusCode == -1) {
      temp.thenThrow(new RuntimeException());
    } else {
      temp.thenReturn(ResponseEntity.status(statusCode).build());
    }

    List<Product> result = sut.getProductsFromCatalog();
    assertThat(result).isEmpty();

    verify(mRestTemplate).exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
        any(Class.class));

    verifyNoMoreInteractions(mRestTemplate, mProducts);
  }

  @Test
  public void testLookupProductsFailedInternalServerError() {
    checkLookupProductsFailed(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @Test
  public void testLookupProductsFailedBadRequest() {
    checkLookupProductsFailed(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void testLookupProductsFailedRedirect() {
    checkLookupProductsFailed(HttpStatus.FOUND.value());
  }

  @Test
  public void testLookupProductsFailedRuntimeException() {
    checkLookupProductsFailed(-1);
  }
}
