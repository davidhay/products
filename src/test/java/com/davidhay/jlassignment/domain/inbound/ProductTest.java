package com.davidhay.jlassignment.domain.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

  @Mock
  Price mPrice;

  Product product;

  @BeforeEach
  public void setup() {
    this.product = new Product();
    product.setPrice(mPrice);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(mPrice);
  }

  @Nested
  class InvalidProducts {

    void checkInvalid(Product p) {
      assertThat(p).isNotNull();
      assertFalse(p.hasValidNowPrice());
    }

    @Test
    void testNullPriceProduct() {
      checkInvalid(new Product());
    }

    @Test
    void testProductWithInvalidPrice() {
      when(mPrice.hasValidNowPrice()).thenReturn(false);
      checkInvalid(product);
      verify(mPrice).hasValidNowPrice();
    }

  }

  @Nested
  class ValidProducts {

    void checkValid(Product p) {
      assertThat(p).isNotNull();
      assertTrue(p.hasValidNowPrice());
    }

    @Test
    void testValidPrice() {
      when(mPrice.hasValidNowPrice()).thenReturn(true);
      checkValid(product);
      verify(mPrice).hasValidNowPrice();
    }
  }
}
