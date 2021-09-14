package com.davidhay.jlassignment.domain.outbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"productId", "title", "colorSwatches", "nowPrice", "priceLabel"})
public class ProductInfo {

  private String productId;

  private String title;

  private List<ColorSwatchInfo> colorSwatches;

  private String nowPrice;

  @Setter
  private String priceLabel;

  @JsonIgnore
  private PriceInfo priceInfo;

  @JsonIgnore
  public boolean hasPriceReduction() {
    return priceInfo.isReduction();
  }

  @JsonIgnore
  public long getPriceReductionAmountPence() {
    return priceInfo.getPriceReductionAmountPence();
  }
}

