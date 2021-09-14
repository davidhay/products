package com.davidhay.jlassignment.repository;

import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductCatalogResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ProductCatalogRepository2 {

    private final RestTemplate restTemplate;
    private final URI baseCatalogURI;
    private final String apiKey;

    public static final String PARAM_NAME_PRODUCT = "q";
    public static final String PARAM_NAME_KEY = "key";

    @Autowired
    public ProductCatalogRepository2(RestTemplate restTemplate,
                                     URI baseCatalogURI,
                                     String apiKey) {
        this.restTemplate = restTemplate;
        this.baseCatalogURI = baseCatalogURI;
        this.apiKey = apiKey;
    }

    public List<Product> getProductsFromCatalog(String product) {
        try {
            HttpHeaders headers = new HttpHeaders();
            // set `accept` header
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // we must provide 'User-Agent'!
            headers.set(HttpHeaders.USER_AGENT, "SpringBootApp");

            // build the request
            HttpEntity<Void> request = new HttpEntity<>(headers);
            HttpHeaders queryParams = new HttpHeaders();
            queryParams.put(PARAM_NAME_PRODUCT, List.of(product));
            queryParams.put(PARAM_NAME_KEY, List.of(apiKey));

            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(baseCatalogURI)
                    .queryParams(queryParams);
            UriComponents uriComponents = builder.build().encode();

            // use `exchange` method for HTTP call
            ResponseEntity<ProductCatalogResponse> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, request, ProductCatalogResponse.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return Collections.emptyList();
            }

            ProductCatalogResponse body = response.getBody();
            if (body == null) {
                return Collections.emptyList();
            }
            List<Product> products = body.getProducts();
            if (products == null) {
                return Collections.emptyList();
            }
            return products.stream().filter(Product::hasValidNowPrice).collect(Collectors.toList());

        } catch (RuntimeException ex) {
            log.error("unexpected inbound error", ex);
            return Collections.emptyList();
        }
    }

}
