package com.davidhay.jlassignment.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.domain.outbound.ProductsInfo;
import java.util.List;
import java.util.Optional;

import com.davidhay.jlassignment.service.ReducedPriceDressesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DressesControllerTest {

  @Mock
  ReducedPriceDressesService mService;

  @InjectMocks
  DressesController sut;

  @Mock
  List<ProductInfo> mProducts;

  @Captor
  ArgumentCaptor<LabelType> argLabelType;

  void check(Optional<LabelType> optLabelType) {

    when(mService.getReducedPriceDresses(argLabelType.capture())).thenReturn(mProducts);
    ProductsInfo result = sut.getReducedPriceDresses(
        optLabelType);

    LabelType expected = (optLabelType == null || optLabelType.isEmpty()) ? LabelType.ShowWasNow : optLabelType.get();

    assertThat(argLabelType.getValue()).isEqualTo(expected);

    assertThat(result.getProducts()).isEqualTo(mProducts);

    verify(mService).getReducedPriceDresses(expected);

    verifyNoMoreInteractions(mService);
  }

  @Test
  public void testNullLabelType() {
    check(null);
  }

  @Test
  public void testNoLabelType() {
    check(Optional.empty());
  }

  @Test
  public void testShowWasThen() {
    check(Optional.of(LabelType.ShowWasNow));
  }

  @Test
  public void testShowWasThenNow() {
    check(Optional.of(LabelType.ShowWasThenNow));
  }

  @Test
  public void testShowPercentageDiscount() {
    check(Optional.of(LabelType.ShowPercDscount));
  }

}
