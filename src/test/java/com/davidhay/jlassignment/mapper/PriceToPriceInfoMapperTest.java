package com.davidhay.jlassignment.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.domain.inbound.Now;
import com.davidhay.jlassignment.domain.inbound.Price;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.mapping.JsonUtils;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PriceToPriceInfoMapperTest {

  final PriceToPriceInfoMapper mapper = new PriceToPriceInfoMapper();

  @Test
  public void testInvalidNowPrice() {
    assertThrows(RuntimeException.class,
        () -> mapper.getPriceInfo(PriceBuilder.builder().to("-10").build()));
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Nested
  class ValidPriceTests {

    @Test
    void testSingleToPriceOnly() {
      Price price = PriceBuilder.builder().to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("10"));
      assertThat(info.getThen()).isEmpty();
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }


    @Test
    void testWasAndTo() {
      Price price = PriceBuilder.builder().was("20").to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("20"));
      assertThat(info.getThen()).isEmpty();
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }

    @Test
    void testWasAndToIgnoreThen() {
      Price price = PriceBuilder.builder().was("20").then1("20").then2("20").to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("20"));
      assertThat(info.getThen()).isEmpty();
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }

    @Test
    void testWasAndToUseThen1() {
      Price price = PriceBuilder.builder().was("21").then1("15").to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("21"));
      assertThat(info.getThen().get()).isEqualByComparingTo(JsonUtils.getAmount("15"));
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }

    @Test
    void testWasAndToUseThen2IgnoreThen1() {
      Price price = PriceBuilder.builder().was("20").then1("20").then2("17").to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("20"));
      assertThat(info.getThen().get()).isEqualByComparingTo(JsonUtils.getAmount("17"));
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }

    @Test
    void testWasAndToUseThen2() {
      Price price = PriceBuilder.builder().was("20").then2("17").to("10").build();
      assertTrue(price.hasValidNowPrice());
      PriceInfo info = mapper.getPriceInfo(price);

      assertThat(info.getWas()).isEqualByComparingTo(JsonUtils.getAmount("20"));
      assertThat(info.getThen().get()).isEqualByComparingTo(JsonUtils.getAmount("17"));
      assertThat(info.getNow()).isEqualByComparingTo(JsonUtils.getAmount("10"));
    }

  }

  static class PriceBuilder {

    private BigDecimal to;
    private BigDecimal was;
    private BigDecimal then1;
    private BigDecimal then2;

    private PriceBuilder() {
    }

    static PriceBuilder builder() {
      return new PriceBuilder();
    }

    PriceBuilder to(String to) {
      this.to = JsonUtils.getAmount(to);
      return this;
    }

    PriceBuilder was(String was) {
      this.was = JsonUtils.getAmount(was);
      return this;
    }

    PriceBuilder then1(String then1) {
      this.then1 = JsonUtils.getAmount(then1);
      return this;
    }

    PriceBuilder then2(String then2) {
      this.then2 = JsonUtils.getAmount(then2);
      return this;
    }

    Price build() {
      Price price = new Price();
      if (this.to != null) {
        Now now = new Now();
        now.setTo(this.to);
        price.setNow(now);
      }
      price.setWas(this.was);
      price.setThen1(this.then1);
      price.setThen2(this.then2);
      return price;
    }

  }

}
