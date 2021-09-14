package com.davidhay.jlassignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.ColorSwatch;
import com.davidhay.jlassignment.domain.inbound.Price;
import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.outbound.ColorSwatchInfo;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.MoneyFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductToProductInfoMapperTest {

  @Mock
  PriceToPriceInfoMapper mPriceInfoService;

  @Mock
  MoneyFormatter mMoneyFormatter;

  @Mock
  ColorSwatchInfoMapper mSwatchInfoMapper;

  @InjectMocks
  ProductToProductInfoMapper sut;

  @Mock
  Price mPrice;

  @Mock
  PriceInfo mPriceInfo;

  @Mock
  ColorSwatch mSwatch1, mSwatch2, mSwatch3;

  @Mock
  ColorSwatchInfo mSwatchInfo1, mSwatchInfo2, mSwatchInfo3;

  @Captor
  ArgumentCaptor<ColorSwatch> argColorSwatch;

  @Test
  void testInvalidProduct() {
    when(mPriceInfoService.getPriceInfo(mPrice)).thenThrow(new RuntimeException());
    when(mPrice.hasValidNowPrice()).thenReturn(true);

    Product p = new Product();
    p.setProductId(UUID.randomUUID().toString());
    p.setTitle(UUID.randomUUID().toString());
    p.setPrice(mPrice);

    assertThrows(RuntimeException.class, () -> sut.mapToProductInfo(p));

    verify(mPriceInfoService).getPriceInfo(mPrice);
  }


  @Test
  void testValidProduct() {
    when(mPriceInfoService.getPriceInfo(mPrice)).thenReturn(mPriceInfo);
    String randomPriceNow = UUID.randomUUID().toString();
    when(mMoneyFormatter.formatNowPrice(mPriceInfo)).thenReturn(randomPriceNow);
    when(mPrice.hasValidNowPrice()).thenReturn(true);

    when(mSwatchInfoMapper.toSwatchInfo(argColorSwatch.capture())).thenReturn(mSwatchInfo1,
        mSwatchInfo2, mSwatchInfo3);

    Product p = new Product();
    p.setColorSwatches(List.of(mSwatch1, mSwatch2, mSwatch3));
    p.setProductId(UUID.randomUUID().toString());
    p.setTitle(UUID.randomUUID().toString());
    p.setPrice(mPrice);

    ProductInfo pInfo = sut.mapToProductInfo(p);

    assertThat(pInfo.getPriceInfo()).isEqualTo(mPriceInfo);
    assertThat(pInfo.getTitle()).isEqualTo(p.getTitle());
    assertThat(pInfo.getProductId()).isEqualTo(p.getProductId());
    assertThat(pInfo.getNowPrice()).isEqualTo(randomPriceNow);

    assertThat(argColorSwatch.getAllValues()).isEqualTo((List.of(mSwatch1, mSwatch2, mSwatch3)));
    assertThat(pInfo.getColorSwatches()).isEqualTo(
        List.of(mSwatchInfo1, mSwatchInfo2, mSwatchInfo3));

    verify(mPriceInfoService).getPriceInfo(mPrice);
    verify(mMoneyFormatter).formatNowPrice(mPriceInfo);
    verify(mSwatchInfoMapper, times(3)).toSwatchInfo(any(ColorSwatch.class));
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(mPriceInfoService, mSwatchInfoMapper);
  }


}


