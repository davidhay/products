package com.davidhay.jlassignment.domain.inbound;

import com.davidhay.jlassignment.mapping.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class PriceDeserializerTest {

  @Autowired
  ObjectMapper mapper;

  @Value("classpath:catalog/price1.json")
  Resource price1;

  @Value("classpath:catalog/price2.json")
  Resource price2;

  Price getPrice(Resource res) throws Exception {
    return mapper.reader().readValue(res.getInputStream(), Price.class);
  }

  @Test
  void testPrice1() throws Exception {
    Price p = getPrice(this.price1);
    checkPrice(p);
  }

  public static void checkPrice(Price p) {
    Assertions.assertThat(p.getWas()).isNull();
    Assertions.assertThat(p.getThen1()).isNull();
    Assertions.assertThat(p.getThen2()).isNull();
    Assertions.assertThat(p.getNow().getFrom()).isEqualTo(JsonUtils.getAmount("9.00"));
    Assertions.assertThat(p.getNow().getTo()).isEqualTo(JsonUtils.getAmount("14.00"));
    Assertions.assertThat(p.getCurrency()).isEqualTo("GBP");
  }

  @Test
  void testPrice2() throws Exception {
    Price p = getPrice(this.price2);
    Assertions.assertThat(p.getWas()).isEqualTo(JsonUtils.getAmount("65"));
    Assertions.assertThat(p.getThen1()).isNull();
    Assertions.assertThat(p.getThen2()).isNull();
    Assertions.assertThat(p.getNow().getFrom()).isNull();
    Assertions.assertThat(p.getNow().getTo()).isEqualTo(JsonUtils.getAmount("32"));
    Assertions.assertThat(p.getCurrency()).isEqualTo("GBP");
  }

}
