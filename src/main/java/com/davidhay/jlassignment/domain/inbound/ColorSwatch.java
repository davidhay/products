package com.davidhay.jlassignment.domain.inbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorSwatch {

  private String color;
  private String basicColor;
  private String skuId;
}
