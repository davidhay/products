package com.davidhay.jlassignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.davidhay.jlassignment.domain.inbound.ColorSwatch;
import com.davidhay.jlassignment.domain.outbound.ColorSwatchInfo;
import com.davidhay.jlassignment.lookup.RgbColorLookup;
import java.util.Optional;
import java.util.UUID;

import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ColorSwatchInfoMapperTest {

  @Mock
  RgbColorLookup mLookup;

  @InjectMocks
  ColorSwatchInfoMapper sut;

  @Captor
  ArgumentCaptor<String> argBasicColor;

  @Test
  void testNoColorMapping() {
    String skuId = UUID.randomUUID().toString();
    String color = UUID.randomUUID().toString();
    String basicColor = UUID.randomUUID().toString();
    ColorSwatch swatch = new ColorSwatch();
    swatch.setSkuId(skuId);
    swatch.setColor(color);
    swatch.setBasicColor(basicColor);

    when(mLookup.getRgbColor(argBasicColor.capture())).thenReturn(Optional.empty());

    ColorSwatchInfo info = sut.toSwatchInfo(swatch);

    assertThat(info.getColor()).isEqualTo(color);
    assertThat(info.getSkuId()).isEqualTo(skuId);
    assertThat(info.getRgbColor()).isNull();

    assertThat(argBasicColor.getValue()).isEqualTo(basicColor);
    verify(mLookup).getRgbColor(basicColor);

    verifyNoMoreInteractions(mLookup);
  }

  @Test
  void testColorMapping() {
    String skuId = UUID.randomUUID().toString();
    String color = UUID.randomUUID().toString();
    String basicColor = UUID.randomUUID().toString();
    ColorSwatch swatch = new ColorSwatch();
    swatch.setSkuId(skuId);
    swatch.setColor(color);
    swatch.setBasicColor(basicColor);

    String rgb = "#123456";
    when(mLookup.getRgbColor(argBasicColor.capture())).thenReturn(Optional.of(rgb));

    ColorSwatchInfo info = sut.toSwatchInfo(swatch);

    assertThat(info.getColor()).isEqualTo(color);
    assertThat(info.getSkuId()).isEqualTo(skuId);
    assertThat(info.getRgbColor()).isEqualTo(rgb);

    assertThat(argBasicColor.getValue()).isEqualTo(basicColor);
    verify(mLookup).getRgbColor(basicColor);

    verifyNoMoreInteractions(mLookup);
  }

  @Test
  void testEmptyColorMapping() {
    ColorSwatch swatch = new ColorSwatch();

    when(mLookup.getRgbColor(null)).thenReturn(Optional.empty());

    ColorSwatchInfo info = sut.toSwatchInfo(swatch);

    assertThat(info.getColor()).isNull();
    assertThat(info.getSkuId()).isNull();
    assertThat(info.getRgbColor()).isNull();

    verify(mLookup).getRgbColor(null);

    verifyNoMoreInteractions(mLookup);
  }

}
