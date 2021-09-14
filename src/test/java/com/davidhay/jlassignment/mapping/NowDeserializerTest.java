package com.davidhay.jlassignment.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import com.davidhay.jlassignment.domain.inbound.Now;
import com.davidhay.jlassignment.util.MoneyUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NowDeserializerTest {

  @Autowired
  ObjectMapper mapper;

  public static class NowWrapper {

    @SuppressWarnings("unused")
    public Now now;
  }

  @Test
  void testNullNow() throws JsonProcessingException {
    NowWrapper wrapper = mapper.readValue("{\"now\":null}", NowWrapper.class);
    assertThat(wrapper.now).isNull();
  }

  @Test
  void testSingleValueNow() throws JsonProcessingException {
    NowWrapper wrapper = mapper.readValue("{\"now\":\"12.34\"}", NowWrapper.class);
    assertThat(wrapper.now.getTo()).isEqualByComparingTo(MoneyUtils.getMoney("12.34"));
  }

  @Test
  void testMultiValueNow() throws JsonProcessingException {
    NowWrapper wrapper = mapper.readValue("{\"now\":{\"from\":\"12.34\",\"to\":\"23.45\"}}",
        NowWrapper.class);
    assertThat(wrapper.now.getFrom()).isEqualByComparingTo(MoneyUtils.getMoney("12.34"));
    assertThat(wrapper.now.getTo()).isEqualByComparingTo(MoneyUtils.getMoney("23.45"));
  }

  @Test
  void testMultiValueNowWithNull() throws JsonProcessingException {
    NowWrapper wrapper = mapper.readValue("{\"now\":{\"to\":\"12.34\"}}", NowWrapper.class);
    assertThat(wrapper.now.getFrom()).isNull();
    assertThat(wrapper.now.getTo()).isEqualByComparingTo(MoneyUtils.getMoney("12.34"));
  }
}
