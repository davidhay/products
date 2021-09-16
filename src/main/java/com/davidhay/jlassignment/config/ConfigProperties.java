package com.davidhay.jlassignment.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "app")
@Setter
@Slf4j
public class ConfigProperties {

    private String baseCatalogURL;

    private String apiKey;

    @Bean
    URI baseCatalogURI() {
        return UriComponentsBuilder.fromUriString(this.baseCatalogURL).build().toUri();
    }

    @Bean
    String apiKey() {
        return apiKey;
    }

    @PostConstruct
    void init() {
        log.info("app.baseCatalogURL  is {}", this.baseCatalogURL);
        String first5 = StringUtils.left(apiKey, 5);
        String last5 = StringUtils.right(apiKey, 5);
        //DO NOT log apiKey to logs!
        log.info("app.apiKey : key : length[{}] : ", apiKey.length());
        log.info("app.apiKey : key : first5[{}] : last5[{}]", first5, last5);
    }
}
