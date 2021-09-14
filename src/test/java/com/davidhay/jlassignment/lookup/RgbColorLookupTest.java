package com.davidhay.jlassignment.lookup;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RgbColorLookupTest {

  final RgbColorLookup lookup = new RgbColorLookup();

  @Test
  public void testRgbLookup() {
    assertEquals(Optional.of("#FF0000"), lookup.getRgbColor("Red"));
    assertEquals(Optional.of("#0000FF"), lookup.getRgbColor("Blue"));
    assertEquals(Optional.of("#00FF00"), lookup.getRgbColor("Green"));
    assertEquals(Optional.of("#FFFFFF"), lookup.getRgbColor("White"));
    assertEquals(Optional.of("#000000"), lookup.getRgbColor("Black"));
    assertEquals(Optional.of("#ADD8E6"), lookup.getRgbColor("Light Blue"));
    assertEquals(Optional.empty(), lookup.getRgbColor("Multi"));
    assertEquals(Optional.empty(), lookup.getRgbColor(null));
  }

  @Test
  void testNormalize() {
    assertThat(lookup.normalize(null)).isNull();
    assertThat(lookup.normalize("Light Blue")).isEqualTo("LIGHTBLUE");
  }

}
