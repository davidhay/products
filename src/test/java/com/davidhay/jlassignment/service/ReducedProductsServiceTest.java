package com.davidhay.jlassignment.service;

import static com.davidhay.jlassignment.controller.LabelType.ShowPercDscount;
import static com.davidhay.jlassignment.controller.LabelType.ShowWasNow;
import static com.davidhay.jlassignment.controller.LabelType.ShowWasThenNow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.PriceLabelFormatter;
import com.davidhay.jlassignment.mapping.JsonUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReducedProductsServiceTest {

  @Mock
  ProductInfoService mProductInfoService;

  @Mock
  PriceLabelFormatter mPriceLabelService;


  ReducedProductsService sut;

  @BeforeEach
  public void checkSetup() {
    //we want to override some internal methods of the service to allow us to test methods in isolation
    sut = Mockito.spy(new ReducedProductsService(mProductInfoService,
        mPriceLabelService));
  }

  @Nested
  class ProcessingStages {

    @Captor
    ArgumentCaptor<List<ProductInfo>> argProductInfo;

    @Captor
    ArgumentCaptor<LabelType> argLabelType;

    @Mock
    List<ProductInfo> mProductInfo1, mProductInfo2, mProductInfo3;

    void checkProcessingStages(LabelType labelType) {

      // setup expectations on these internal methods
      doReturn(mProductInfo1).when(sut).getProductInfo();
      doReturn(mProductInfo2).when(sut).getReducedProductInfoStream(argProductInfo.capture());
      doReturn(mProductInfo3).when(sut)
          .labelReducedProductInfos(argLabelType.capture(), argProductInfo.capture());

      //invoke the method we are testing
      List<ProductInfo> result = sut.getReducedPriceDresses(labelType);

      assertThat(result).isEqualTo(mProductInfo3);
      assertThat(argProductInfo.getAllValues().size()).isEqualTo(2);
      assertThat(argProductInfo.getAllValues().get(0)).isEqualTo(mProductInfo1);
      assertThat(argProductInfo.getAllValues().get(1)).isEqualTo(mProductInfo2);
      assertThat(argLabelType.getValue()).isEqualTo(labelType);

      verify(sut).getProductInfo();
      verify(sut).getReducedProductInfoStream(mProductInfo1);
      verify(sut).labelReducedProductInfos(labelType, mProductInfo2);

      //verify method under test because it's also a spy
      verify(sut).getReducedPriceDresses(labelType);

      verifyNoMoreInteractions(sut, mProductInfo1, mProductInfo2, mProductInfo3);
    }

    @Test
    void testShowWasNow() {
      checkProcessingStages(ShowWasNow);
    }

    @Test
    void testShowWasThenNow() {
      checkProcessingStages(ShowWasThenNow);
    }

    @Test
    void testShowPercentageDiscount() {
      checkProcessingStages(ShowPercDscount);
    }

  }

  @Nested
  class Stage1ofwGetProductInfo {

    @Mock
    List<ProductInfo> mProductInfo1;


    @Test
    void testGetProducts() {
      when(mProductInfoService.getProductInfo()).thenReturn(mProductInfo1);

      List<ProductInfo> result = sut.getProductInfo();
      assertThat(result).isEqualTo(mProductInfo1);

      verify(mProductInfoService).getProductInfo();

      //verify method under test because it's also a spy
      verify(sut).getProductInfo();

      verifyNoMoreInteractions(mProductInfo1, sut, mProductInfoService);
    }
  }


  @Nested
  class Stage2of3 {

    private final ProductInfo nonRedWas10now20 = getProductInfo("10", "20");
    private final ProductInfo nonRedWas10now10 = getProductInfo("10", "20");
    private final ProductInfo nonRedWas20now20 = getProductInfo("10", "20");
    private final ProductInfo reducedWas20now10 = getProductInfo("20", "10");
    private final ProductInfo reducedWas20now15 = getProductInfo("20", "15");
    private final ProductInfo reducedWas20now5 = getProductInfo("20", "5");

    private ProductInfo getProductInfo(String was, String now) {
      PriceInfo price = new PriceInfo("blah", JsonUtils.getAmount(was), JsonUtils.getAmount(now));
      return ProductInfo.builder().priceInfo(price).build();
    }

    @Test
    void testFilterAndSort() {

      List<ProductInfo> products = Arrays.asList(
          nonRedWas10now10, reducedWas20now15,
          nonRedWas10now20, reducedWas20now10,
          nonRedWas20now20, reducedWas20now5);

      assertTrue(reducedWas20now5.hasPriceReduction());
      assertTrue(reducedWas20now15.hasPriceReduction());
      assertTrue(reducedWas20now10.hasPriceReduction());

      assertFalse(nonRedWas10now10.hasPriceReduction());
      assertFalse(nonRedWas10now20.hasPriceReduction());
      assertFalse(nonRedWas20now20.hasPriceReduction());

      List<ProductInfo> result = sut.getReducedProductInfoStream(products);

      assertThat(result).isEqualTo(
          List.of(reducedWas20now5, reducedWas20now10, reducedWas20now15));
    }
  }

  @Nested
  class Stage3of3Labels {

    @Captor
    ArgumentCaptor<PriceInfo> argPriceInfo;

    @Captor
    ArgumentCaptor<LabelType> argLabelType;

    @Mock
    PriceInfo mPriceInfo1, mPriceInfo2, mPriceInfo3;

    ProductInfo productInfo1, productInfo2, productInfo3;

    @BeforeEach
    void setup() {
      productInfo1 = ProductInfo.builder().priceInfo(mPriceInfo1).build();
      productInfo2 = ProductInfo.builder().priceInfo(mPriceInfo2).build();
      productInfo3 = ProductInfo.builder().priceInfo(mPriceInfo3).build();
    }

    void checkAddLabelsForType(LabelType labelType) {

      //setup behavour for mLabelServiceMock
      when(mPriceLabelService.getPriceLabel(argPriceInfo.capture(),
          argLabelType.capture())).thenReturn("label1", "label2", "label3");

      List<ProductInfo> productInfo = List.of(productInfo1, productInfo2, productInfo3);

      //invoke method being tested
      List<ProductInfo> result = sut.labelReducedProductInfos(labelType, productInfo);

      assertThat(result).isEqualTo(List.of(productInfo1, productInfo2, productInfo3));
      assertThat(
          result.stream().map(ProductInfo::getPriceLabel).collect(Collectors.toList())).isEqualTo(
          List.of("label1", "label2", "label3"));

      //check args were supplied to mock as expected
      assertThat(argPriceInfo.getAllValues()).isEqualTo(
          List.of(mPriceInfo1, mPriceInfo2, mPriceInfo3));
      assertThat(argLabelType.getAllValues()).isEqualTo(List.of(labelType, labelType, labelType));

      //verify calls to mock
      verify(mPriceLabelService, times(3)).getPriceLabel(any(PriceInfo.class),
          any(LabelType.class));
      verify(sut, times(1)).labelReducedProductInfos(labelType, productInfo);

      verifyNoMoreInteractions(sut, mPriceLabelService, mProductInfoService);
    }

    @Test
    void testLabellingForWasNow() {
      checkAddLabelsForType(ShowWasNow);
    }

    @Test
    void testLabellingForWasThenNow() {
      checkAddLabelsForType(ShowWasThenNow);
    }

    @Test
    void testLabellingForPercentageDiscount() {
      checkAddLabelsForType(ShowPercDscount);
    }
  }

}
