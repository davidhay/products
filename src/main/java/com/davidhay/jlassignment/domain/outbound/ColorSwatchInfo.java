package com.davidhay.jlassignment.domain.outbound;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonPropertyOrder({"color", "rgbColor", "skuId"})
public class ColorSwatchInfo {

  private String color;
  private String rgbColor;
  private String skuId;
}
