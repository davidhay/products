package com.davidhay.jlassignment.domain.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PriceTest {

  @Nested
  class InvalidPrices {

    void checkInvalid(Price p) {
      assertThat(p).isNotNull();
      assertFalse(p.hasValidNowPrice());
    }

    @Test
    void testEmptyProduct() {
      checkInvalid(new Price());
    }

    @Test
    void testPriceWithEmptyNow() {
      Price price = new Price();
      Now now = new Now();
      price.setNow(now);
      checkInvalid(price);
    }

    @Test
    void testPriceWithNegativeNowTo() {
      Price price = new Price();
      Now now = new Now();
      now.setTo(BigDecimal.TEN.negate());
      price.setNow(now);
      checkInvalid(price);
    }

    @Test
    void testPriceWithZeroNowTo() {
      Price price = new Price();
      Now now = new Now();
      now.setTo(BigDecimal.ZERO);
      price.setNow(now);
      checkInvalid(price);
    }
  }

  @Nested
  class ValidPrice {

    void checkValid(Price p) {
      assertThat(p).isNotNull();
      assertTrue(p.hasValidNowPrice());
    }

    @Test
    void testPriceWithPositiveNowTo() {
      Price price = new Price();
      Now now = new Now();
      now.setTo(BigDecimal.ONE);
      price.setNow(now);
      checkValid(price);
    }
  }
}
