package com.davidhay.jlassignment.domain.inbound;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  private String productId;

  private String title;

  private Price price;

  private List<ColorSwatch> colorSwatches;

  public boolean hasValidNowPrice() {
    final boolean result;
    if (price == null) {
      result = false;
    } else {
      result = price.hasValidNowPrice();
    }
    return result;
  }
}
