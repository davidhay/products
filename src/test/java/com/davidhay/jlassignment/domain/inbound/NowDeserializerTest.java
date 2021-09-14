package com.davidhay.jlassignment.domain.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import com.davidhay.jlassignment.mapping.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class NowDeserializerTest {

  @Autowired
  ObjectMapper mapper;

  @Value("classpath:catalog/now1.json")
  Resource now1;

  @Value("classpath:catalog/now2.json")
  Resource now2;

  Now getNow(Resource res) throws Exception {
    return mapper.reader().readValue(res.getInputStream(), Now.class);
  }

  @Test
  void tesNow1() throws Exception {
    Now now = getNow(this.now1);
    Assertions.assertThat(JsonUtils.getAmount("9.00")).isEqualTo(now.getFrom());
    assertThat(JsonUtils.getAmount("14.00")).isEqualTo(now.getTo());
  }

  @Test
  void tesNow2() throws Exception {
    Now now = getNow(this.now2);
    assertThat(now.getFrom()).isNull();
    assertThat(JsonUtils.getAmount("32.00")).isEqualTo(now.getTo());
  }
}
