package com.davidhay.jlassignment.config;

import com.davidhay.jlassignment.domain.inbound.Now;
import com.davidhay.jlassignment.formatter.MoneyFormatter;
import com.davidhay.jlassignment.lookup.RgbColorLookup;
import com.davidhay.jlassignment.mapping.BigDecimalDeserializer;
import com.davidhay.jlassignment.mapping.NowDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class AppConfig {

  private Module getModule() {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
    module.addDeserializer(Now.class, new NowDeserializer());
    return module;
  }

  @Bean
  @Primary
  public ObjectMapper mapper() {
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(getModule());
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    return mapper;
  }

  @Bean
  public MoneyFormatter moneyFormatter() {
    return new MoneyFormatter();
  }

  @Bean
  @Primary
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

    return restTemplateBuilder
        .setReadTimeout(Duration.ofSeconds(10))
        .setConnectTimeout(Duration.ofSeconds(10))
        .build();
  }

  @Bean
  RgbColorLookup lookup() {
    return new RgbColorLookup();
  }


}
