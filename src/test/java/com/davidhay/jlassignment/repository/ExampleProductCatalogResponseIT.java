package com.davidhay.jlassignment.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.davidhay.jlassignment.domain.inbound.ColorSwatch;
import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.inbound.ProductType;
import com.davidhay.jlassignment.lookup.RgbColorLookup;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest
public class ExampleProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  ProductCatalogRepository repo;

  @Autowired
  RgbColorLookup lookup;

  @Test
  public void testExampleResponse() {

    //get the products from the example file
    List<Product> products = repo.getProductsFromCatalog(ProductType.DRESSES);

    //check the number of products
    assertThat(products.size()).isEqualTo(24);

    //check that we only have 'Multi' as the only basicColor without an rgb value
    Set<String> missingColors = new HashSet<>();
    products.forEach(product -> product.getColorSwatches().stream().map(ColorSwatch::getBasicColor)
        .forEach(basic -> {
          if (lookup.getRgbColor(basic).isEmpty()) {
            missingColors.add(basic);
          }
        }));
    assertThat(Set.of("Multi")).isEqualTo(missingColors);
  }

  @Override
  public String[] getStubs() {
    return Stubs.EXAMPLE_STUBS;
  }


}
