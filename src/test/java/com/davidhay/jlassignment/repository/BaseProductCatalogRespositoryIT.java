package com.davidhay.jlassignment.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.WireMockRestServiceServer;
import org.springframework.core.env.Environment;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
public abstract class BaseProductCatalogRespositoryIT {

  @Autowired
  Environment environment;

  @Autowired
  RestTemplate restTemplate;

  @BeforeEach
  void setup() {
    //noinspection unused
    MockRestServiceServer mockRestServiceServer = WireMockRestServiceServer.with(this.restTemplate)
        .stubs(getStubs())
        .build();
  }

  public abstract String[] getStubs();

  @PostConstruct
  void checkProfileIsTest() {
    assertTrue(Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toList())
        .contains("test"));
  }


}
