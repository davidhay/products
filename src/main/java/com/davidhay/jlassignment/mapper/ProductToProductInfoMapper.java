package com.davidhay.jlassignment.mapper;

import com.davidhay.jlassignment.domain.inbound.ColorSwatch;
import com.davidhay.jlassignment.domain.inbound.Product;
import com.davidhay.jlassignment.domain.outbound.ColorSwatchInfo;
import com.davidhay.jlassignment.domain.outbound.PriceInfo;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.MoneyFormatter;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * This class performs the mapping from Product to ProductInfo except for the priceLabel which is
 * added later.
 */
@Component
public class ProductToProductInfoMapper {

  private final PriceToPriceInfoMapper priceInfoService;
  private final MoneyFormatter moneyFormatter;
  private final ColorSwatchInfoMapper colorSwatchInfoMapper;

  public ProductToProductInfoMapper(PriceToPriceInfoMapper priceInfoService,
      MoneyFormatter moneyFormatter, ColorSwatchInfoMapper colorSwatchInfoMapper) {
    this.priceInfoService = priceInfoService;
    this.moneyFormatter = moneyFormatter;
    this.colorSwatchInfoMapper = colorSwatchInfoMapper;
  }

  public ProductInfo mapToProductInfo(Product product) {
    Preconditions.checkArgument(product.hasValidNowPrice());
    PriceInfo priceInfo = priceInfoService.getPriceInfo(product.getPrice());
    String priceNow = moneyFormatter.formatNowPrice(priceInfo);
    return
        ProductInfo.builder()
            .productId(product.getProductId())
            .title(product.getTitle())
            .priceInfo(priceInfo)
            .nowPrice(priceNow)
            .colorSwatches(getColorSwatchInfos(product.getColorSwatches()))
            .build();
  }

  private List<ColorSwatchInfo> getColorSwatchInfos(List<ColorSwatch> swatches) {
    if (swatches == null) {
      return Collections.emptyList();
    }
    return swatches
        .stream()
        .filter(Objects::nonNull)
        .map(colorSwatchInfoMapper::toSwatchInfo)
        .collect(Collectors.toList());
  }

}
