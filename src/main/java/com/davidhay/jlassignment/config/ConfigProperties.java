package com.davidhay.jlassignment.config;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@ConfigurationProperties(prefix = "app")
@Setter
@Slf4j
public class ConfigProperties {

  private String catalogURL;

  private String baseCatalogURL;

  private String apiKey;

  private Environment environment;

  @Autowired
  ConfigProperties(Environment environment) {
    this.environment = environment;
  }

  @Bean
  URL catalogURL() throws MalformedURLException {

    URI uri = UriComponentsBuilder.fromUriString(this.catalogURL).build().toUri();

    URL base = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath());
    log.info("app.catalogURL - base url is {}", base.toExternalForm());

    boolean isProduction = Arrays.stream(environment.getActiveProfiles())
        .collect(Collectors.toList()).contains("production");
    List<NameValuePair> params = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);

    Map<String, NameValuePair> paramsByName = params.stream()
        .collect(Collectors.toMap(NameValuePair::getName,
            nvp -> nvp));

    log.info("app.catalogURL - query string params {}", paramsByName.keySet());

    if (isProduction) {
      NameValuePair pairQ = paramsByName.get("q");
      NameValuePair pairKey = paramsByName.get("key");

      //we sanity check query string params
      Assert.isTrue(pairQ.getValue().equals("dresses"),
          "app.catalogURL - The 'q' query string does not have value 'dresses'");
      Assert.isTrue(!pairKey.getValue().isBlank(), "inbound.url - The 'key' must not be blank");
      Assert.isTrue(!pairKey.getValue().matches("//s"),
          "app.catalogURL - the 'key' must NOT contain whitespace");
      String key = pairKey.getValue();
      String first5 = StringUtils.left(key, 5);
      String last5 = StringUtils.right(key, 5);
      log.info("app.catalogURL : query parameter : q   : value[{}]", pairQ.getValue());
      //The 'key' is a secret - we should not log it to console
      log.info("app.catalogURL : query parameter : key : length[{}] : ", key.length());
      log.info("app.catalogURL : query parameter : key : first5[{}] : last5[{}]", first5, last5);
    } else {
      assert (params.isEmpty());
    }
    return uri.toURL();
  }

  @Bean
  URI baseCatalogURI() {
    return UriComponentsBuilder.fromUriString(this.baseCatalogURL).build().toUri();
  }

  @Bean
  String apiKey() {
    return apiKey;
  }

}
