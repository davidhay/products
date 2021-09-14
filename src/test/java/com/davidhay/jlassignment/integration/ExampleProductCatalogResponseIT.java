package com.davidhay.jlassignment.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.controller.DressesController;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import com.davidhay.jlassignment.repository.BaseProductCatalogRespositoryIT;
import com.davidhay.jlassignment.repository.Stubs;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExampleProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  public void testExampleResponseFromCatalog() {
    ResponseEntity<ProductsInfo> response = restTemplate.getForEntity(DressesController.REDUCED_PRICES_DRESSES_PATH,
        ProductsInfo.class);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    Objects.requireNonNull(response.getHeaders().getContentType());
    assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
    ProductsInfo info = response.getBody();
    assertThat(info).isNotNull();
    List<ProductInfo> products = info.getProducts();
    assertThat(products).isNotNull();
    assertThat(products).size().isEqualTo(10);

    Map<String, Integer> numColorsByProductId = products.stream().collect(Collectors.toMap(
        ProductInfo::getProductId,
        productInfo -> productInfo.getColorSwatches().size()));

    Map<String, Integer> expectedNumColorsByProductId = Map.of(
        "5445887", 1,
        "5540000", 1,
        "5539847", 1,
        "5455696", 1,
        "5556086", 1,
        "5302138", 1,
        "5530151", 1,
        "5530101", 1,
        "5444166", 1,
        "5591799", 1
    );

    assertEquals(numColorsByProductId, expectedNumColorsByProductId);

    Map<String, List<Pair<String, String>>> colorsByProduct = new TreeMap<>(
        products.stream().collect(Collectors.toMap(
            ProductInfo::getProductId,
            productInfo -> productInfo.getColorSwatches()
                .stream()
                .map(pi -> Pair.of(pi.getColor(), pi.getRgbColor()))
                .collect(Collectors.toList())
        )));

    Map<String, List<Pair<String, String>>> expectedColorsByProduct = new TreeMap<>(Map.of(
        "5445887", List.of(Pair.of("Blue", "#0000FF")),
        "5540000", List.of(Pair.of("Pale Green", "#00FF00")),
        "5539847", List.of(Pair.of(null, null)),
        "5455696", List.of(Pair.of(null, "#000000")),
        "5556086", List.of(Pair.of(null, "#00FF00")),
        "5302138", List.of(Pair.of(null, "#A52A2A")),
        "5530151", List.of(Pair.of(null, null)),
        "5530101", List.of(Pair.of(null, "#FFFF00")),
        "5444166", List.of(Pair.of(null, null)),
        "5591799", List.of(Pair.of(null, null))
    ));

    assertEquals(colorsByProduct, expectedColorsByProduct);

  }

  @Override
  public String[] getStubs() {
    return Stubs.EXAMPLE_STUBS;
  }


}
