package com.davidhay.jlassignment.mapper;

import com.davidhay.jlassignment.domain.inbound.ColorSwatch;
import com.davidhay.jlassignment.domain.outbound.ColorSwatchInfo;
import com.davidhay.jlassignment.lookup.RgbColorLookup;
import org.springframework.stereotype.Component;

@Component
public class ColorSwatchInfoMapper {

  private final RgbColorLookup lookup;

  public ColorSwatchInfoMapper(RgbColorLookup lookup) {
    this.lookup = lookup;
  }

  ColorSwatchInfo toSwatchInfo(ColorSwatch swatch) {

    return ColorSwatchInfo.builder()
        .skuId(swatch.getSkuId())
        .color(normalizeColor(swatch.getColor()))
        .rgbColor(lookupRgb(swatch.getBasicColor()))
        .build();
  }

  private String lookupRgb(String basicColor) {
    return lookup.getRgbColor(basicColor).orElse(null);
  }

  String normalizeColor(String color) {
    if (color == null) {
      return null;
    }
    String trimmed = color.trim();
    if (trimmed.isBlank()) {
      return null;
    }
    return trimmed;
  }


}
