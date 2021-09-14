package com.davidhay.jlassignment.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.davidhay.jlassignment.controller.DressesController;
import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ColorSwatchInfo;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import com.davidhay.jlassignment.repository.BaseProductCatalogRespositoryIT;
import com.davidhay.jlassignment.repository.Stubs;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This integration test gets data from a wiremock server using stubbed data.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SingleProductCatalogResponseIT extends BaseProductCatalogRespositoryIT {

  @Autowired
  TestRestTemplate restTemplate;

  List<ProductInfo> getProductInfo(Optional<LabelType> optLabelType) {

    UriComponentsBuilder builder = UriComponentsBuilder.fromPath(DressesController.REDUCED_PRICES_DRESSES_PATH);
    if (optLabelType.isPresent()) {
      builder = builder.queryParam(DressesController.LABEL_TYPE, optLabelType.get());
    }
    URI uri = builder.build().toUri();

    ResponseEntity<ProductsInfo> response = restTemplate.getForEntity(uri, ProductsInfo.class);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertThat(response.getHeaders().getContentType()).isNotNull();
    assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
    ProductsInfo info = response.getBody();
    assertThat(info).isNotNull();
    return info.getProducts();
  }

  @Override
  public String[] getStubs() {
    return Stubs.SINGLE_PRODUCT_STUBS;
  }

  @Nested
  class LabelTypeChecks {

    @Test
    public void testSingleResponseNoLabelSpecified() {
      List<ProductInfo> products = getProductInfo(Optional.empty());
      assertThat(products).isNotNull();
      assertThat(products).size().isEqualTo(1);
      checkWasNow(products.get(0));
    }

    @Test
    public void testSingleResponseWasNowLabelSpecified() {
      List<ProductInfo> products = getProductInfo(Optional.of(LabelType.ShowWasNow));
      assertThat(products).isNotNull();
      assertThat(products).size().isEqualTo(1);
      checkWasNow(products.get(0));
    }


    private void checkWasNow(ProductInfo productInfo) {
      assertEquals("5520412", productInfo.getProductId());
      assertEquals("John Lewis & Partners School Belted Gingham Checked Summer Dress",
          productInfo.getTitle());
      assertEquals("Was £14.14, now £14", productInfo.getPriceLabel());
      assertEquals("£14", productInfo.getNowPrice());
    }

    @Test
    public void testSingleResponseWasThenNowLabelSpecified() {
      List<ProductInfo> products = getProductInfo(Optional.of(LabelType.ShowWasThenNow));
      assertThat(products).isNotNull();
      assertThat(products).size().isEqualTo(1);
      checkWasThenNow(products.get(0));
    }

    private void checkWasThenNow(ProductInfo productInfo) {
      assertEquals("5520412", productInfo.getProductId());
      assertEquals("John Lewis & Partners School Belted Gingham Checked Summer Dress",
          productInfo.getTitle());
      assertEquals("Was £14.14, then £14.05, now £14", productInfo.getPriceLabel());
      assertEquals("£14", productInfo.getNowPrice());
    }

    @Test
    public void testSingleResponsePercentageDiscountSpecified() {
      List<ProductInfo> products = getProductInfo(Optional.of(LabelType.ShowPercDscount));
      assertThat(products).isNotNull();
      assertThat(products).size().isEqualTo(1);
      checkPercentageDiscount(products.get(0));
    }

    private void checkPercentageDiscount(ProductInfo productInfo) {
      assertEquals("5520412", productInfo.getProductId());
      assertEquals("John Lewis & Partners School Belted Gingham Checked Summer Dress",
          productInfo.getTitle());
      assertEquals("1% off - now £14", productInfo.getPriceLabel());
      assertEquals("£14", productInfo.getNowPrice());
    }

  }

  @Nested
  class ColorSwatchChecks {

    @Test
    void testColorSwatches() {
      List<ProductInfo> products = getProductInfo(Optional.empty());
      assertThat(products).isNotNull();
      assertThat(products).size().isEqualTo(1);

      ProductInfo productInfo = products.get(0);
      List<ColorSwatchInfo> swatches = productInfo.getColorSwatches();
      assertEquals(7, swatches.size());

      ColorSwatchInfo swatchInfo1 = ColorSwatchInfo.builder().skuId("239018760").rgbColor("#FFFF00")
          .color("Yellow").build();
      ColorSwatchInfo swatchInfo2 = ColorSwatchInfo.builder().skuId("239018691").rgbColor("#00FF00")
          .color("Green").build();
      ColorSwatchInfo swatchInfo3 = ColorSwatchInfo.builder().skuId("239018687").rgbColor("#800080")
          .color("Lilac").build();
      ColorSwatchInfo swatchInfo4 = ColorSwatchInfo.builder().skuId("239018713").rgbColor("#0000FF")
          .color("Navy").build();
      ColorSwatchInfo swatchInfo5 = ColorSwatchInfo.builder().skuId("239018723").rgbColor("#FFC0CB")
          .color("Pink").build();
      ColorSwatchInfo swatchInfo6 = ColorSwatchInfo.builder().skuId("239018816").rgbColor("#FF0000")
          .color("Red").build();
      ColorSwatchInfo swatchInfo7 = ColorSwatchInfo.builder().skuId("239018712").rgbColor("#0000FF")
          .color("Light Blue").build();

      assertThat(swatches.get(0)).isEqualTo(swatchInfo1);
      assertThat(swatches.get(1)).isEqualTo(swatchInfo2);
      assertThat(swatches.get(2)).isEqualTo(swatchInfo3);
      assertThat(swatches.get(3)).isEqualTo(swatchInfo4);
      assertThat(swatches.get(4)).isEqualTo(swatchInfo5);
      assertThat(swatches.get(5)).isEqualTo(swatchInfo6);
      assertThat(swatches.get(6)).isEqualTo(swatchInfo7);

    }

  }

}
