package com.davidhay.jlassignment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductCatalogResponse;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

  public static final String FAKE_URL = "https://someserver/path";

  @Mock
  RestTemplate mRestTemplate;

  String apiKey;

  URI productCatalogURI;

  ProductCatalogRepository sut;

  @Mock
  Product mProduct1,mProduct2,mProduct3;

  @Captor
  ArgumentCaptor<String> argURL;
  @Captor
  ArgumentCaptor<HttpEntity<Void>> argRequest;
  @Captor
  ArgumentCaptor<HttpMethod> argMethod;
  @Captor
  ArgumentCaptor<Class<ProductCatalogResponse>> argClass;

  @BeforeEach
  void setup() {
    apiKey = UUID.randomUUID().toString();
    productCatalogURI = UriComponentsBuilder.fromUriString(FAKE_URL).build().toUri();
    sut = new ProductCatalogRepository(mRestTemplate, productCatalogURI, apiKey);
  }

  @Test
  public void testLookupProducts() {
    ProductCatalogResponse response = new ProductCatalogResponse();
    response.setProducts(List.of(mProduct1, mProduct2, mProduct3));

    when(mProduct1.hasValidNowPrice()).thenReturn(true);
    when(mProduct2.hasValidNowPrice()).thenReturn(false);
    when(mProduct3.hasValidNowPrice()).thenReturn(true);

    when(mRestTemplate.exchange(argURL.capture(), argMethod.capture(), argRequest.capture(),
        argClass.capture())).thenReturn(
        ResponseEntity.ok(response));

    List<Product> result = sut.getProductsFromCatalog("dresses");
    assertThat(result).isEqualTo(List.of(mProduct1, mProduct3));

    assertThat(argURL.getValue()).isEqualTo(String.format("%s%s%s",FAKE_URL,"?q=dresses&key=",apiKey));
    verify(mRestTemplate).exchange(argURL.getValue(), argMethod.getValue(), argRequest.getValue(),
        argClass.getValue());

    verify(mProduct1).hasValidNowPrice();
    verify(mProduct2).hasValidNowPrice();
    verify(mProduct3).hasValidNowPrice();

    verifyNoMoreInteractions(mRestTemplate, mProduct1, mProduct2, mProduct3);
  }

  void checkLookupProductsFailed(int statusCode) {
    ProductCatalogResponse response = new ProductCatalogResponse();
    response.setProducts(List.of(mProduct1, mProduct2, mProduct3));

    OngoingStubbing<ResponseEntity<ProductCatalogResponse>> temp = when(
        mRestTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
            any(Class.class)));

    if (statusCode == -1) {
      temp.thenThrow(new RuntimeException());
    } else {
      temp.thenReturn(ResponseEntity.status(statusCode).build());
    }

    List<Product> result = sut.getProductsFromCatalog("dresses");
    assertThat(result).isEmpty();

    verify(mRestTemplate).exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
        any(Class.class));

    verifyNoMoreInteractions(mRestTemplate, mProduct1, mProduct2, mProduct3);
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
